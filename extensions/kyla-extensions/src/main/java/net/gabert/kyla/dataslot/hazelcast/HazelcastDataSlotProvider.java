package net.gabert.kyla.dataslot.hazelcast;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.NearCacheConfig;
import com.hazelcast.core.*;
import net.gabert.kyla.api.DataSlotProvider;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import net.gabert.kyla.api.Endpoint;
import net.gabert.kyla.api.Endpoint.Message;
import net.gabert.kyla.dataslot.WorkUnitProcessor;

public class HazelcastDataSlotProvider implements DataSlotProvider {
    private final static String ID_DELIMITER= ":";

    private final static String SHARED_DATASLOTS_ID = "SHARED_DATASLOTS";
    private final MultiMap<String, String> sharedDataSlots;

    private final static String EXCLUSIVE_DATASLOTS_ID = "EXCLUSIVE_DATASLOTS_ID";
    private final IMap<String, String> exclusiveDataSlots;

    private final ConcurrentHashMap<String, Endpoint> localEndpointRegistrations;

    private final String TOPIC_NAME;
    private final ITopic<Message> clusterMemberTopic;

    private final HazelcastInstance hazelcastInstance;
    private final String clusterMemberId;

    private final WorkUnitProcessor workUnitProcessor;

    private HazelcastDataSlotProvider(WorkUnitProcessor workUnitProcessor) {
        this.workUnitProcessor = workUnitProcessor;

        Config cfg              = new Config();
        addNearCache(cfg);

        this.hazelcastInstance  = Hazelcast.newHazelcastInstance(cfg);
        this.clusterMemberId    = this.hazelcastInstance.getCluster().getLocalMember().getUuid();
        this.TOPIC_NAME         = this.clusterMemberId;

        this.sharedDataSlots    = this.hazelcastInstance.getMultiMap(SHARED_DATASLOTS_ID);
        this.exclusiveDataSlots = this.hazelcastInstance.getMap(EXCLUSIVE_DATASLOTS_ID);
        this.clusterMemberTopic = this.hazelcastInstance.getTopic(TOPIC_NAME);

        this.localEndpointRegistrations = new ConcurrentHashMap<>();

        this.clusterMemberTopic.addMessageListener(new TopicListener());
    }

    private static void addNearCache(Config config) {
        NearCacheConfig ncc = new NearCacheConfig();
        ncc.setEvictionPolicy("LRU");
        ncc.setMaxSize(500000);
        ncc.setInvalidateOnChange(true);

        MapConfig mapConfig = new MapConfig();
        mapConfig.setNearCacheConfig(ncc);

        Map<String, MapConfig> mapConfigs = new HashMap<>();
        mapConfigs.put(EXCLUSIVE_DATASLOTS_ID, mapConfig);

        config.setMapConfigs(mapConfigs);
    }

    public static DataSlotProvider getInstance(WorkUnitProcessor workUnitProcessor) {
        return new HazelcastDataSlotProvider(workUnitProcessor);
    }

    @Override
    public void shutdown() {
        hazelcastInstance.shutdown();
    }

    @Override
    public synchronized void register(Endpoint endpoint) {
        String endpointDataSlot = endpoint.getDataSlotId();
        String globalEndpointId = clusterMemberId + ID_DELIMITER + endpointDataSlot;

        if (exclusiveDataSlots.containsKey(endpointDataSlot)) {
            throw new IllegalStateException("Endpoint[" +
                    exclusiveDataSlots.get(endpointDataSlot) +
                    "] already registered with dataSlot[" +
                    endpointDataSlot +
                    "]");
        }

        localEndpointRegistrations.putIfAbsent(globalEndpointId, endpoint);
        exclusiveDataSlots.put(endpointDataSlot, globalEndpointId);
    }

    @Override
    public synchronized void registerExclusive(Endpoint endpoint, String dataSlotId) {
        String globalEndpointId = clusterMemberId + ID_DELIMITER + endpoint.getDataSlotId();

        if (exclusiveDataSlots.containsKey(dataSlotId)) {
            throw new IllegalStateException("Endpoint[" +
                    exclusiveDataSlots.get(dataSlotId) +
                    "] already registered with dataSlot[" +
                    dataSlotId +
                    "]");
        }

        String endpointDataSlot = endpoint.getDataSlotId();
        if (exclusiveDataSlots.containsKey(endpointDataSlot) == false) {
            register(endpoint);
        }

        localEndpointRegistrations.putIfAbsent(globalEndpointId, endpoint);
        exclusiveDataSlots.put(dataSlotId, globalEndpointId);
    }

    @Override
    public synchronized void register(Endpoint endpoint, String dataSlotId) {
        String globalEndpointId = clusterMemberId + ID_DELIMITER + endpoint.getDataSlotId();

        String endpointDataSlot = endpoint.getDataSlotId();
        if (exclusiveDataSlots.containsKey(endpointDataSlot) == false) {
            register(endpoint);
        }

        localEndpointRegistrations.putIfAbsent(globalEndpointId, endpoint);
        sharedDataSlots.put(dataSlotId, globalEndpointId);
    }

    private class TopicListener implements MessageListener<Message> {
        @Override
        public void onMessage(com.hazelcast.core.Message<Message> message) {
            // Topic listener received message from remote cluster member
            // Routing was already done by remote cluster member meaning further routing must not occur
            // Topic listener must consult only local endpoint registration and create workunits
            Message kylaMessage = message.getMessageObject();
            // FIXME: wrap kyla message into structure together with destination slot ID;
            redistribute(kylaMessage, kylaMessage.getDestinationSlotId(), false);
        }
    }

    @Override
    public void distribute(Message message, String destinationDataSlotId) {
        redistribute(message, destinationDataSlotId, true);
    }

    private void redistribute(Message message, String destinationDataSlotId, boolean routingEnabled) {
        List<String> globalEndpointIds = resolveGlobalEndpointIds(destinationDataSlotId);
        redistribute(globalEndpointIds, message, routingEnabled);
    }

    private void redistribute(List<String> globalEndpointIds, Message message, boolean routingEnabled) {
        Set<String> remoteTopics = new TreeSet<>();

        for (String globalEndpointId : globalEndpointIds) {
            String destinationTopicId = getClusterMemberId(globalEndpointId);

            if ( destinationReached(destinationTopicId) ) {
                createWorkUnit(message, globalEndpointId);
            } else if ( routingEnabled ){
                remoteTopics.add(destinationTopicId);
            }
        }

        for (String destinationTopicId : remoteTopics) {
            this.hazelcastInstance.getTopic(destinationTopicId).publish(message);
        }
    }

    private List<String> resolveGlobalEndpointIds(String destinationDataSlotId) {
        String globalEndpointId = exclusiveDataSlots.get(destinationDataSlotId);
        if (globalEndpointId != null) {
            int numOfEndpoints = 1;
            List<String> globalEndpointIds = new ArrayList<>(numOfEndpoints);
            globalEndpointIds.add(globalEndpointId);
            return globalEndpointIds;
        }

        Collection<String> sharedDataSlotEndpoints = sharedDataSlots.get(destinationDataSlotId);
        if (sharedDataSlotEndpoints != null) {
            int numOfEndpoints = sharedDataSlotEndpoints.size();
            List<String> globalEndpointIds = new ArrayList<>(numOfEndpoints);
            globalEndpointIds.addAll(sharedDataSlotEndpoints);
            return globalEndpointIds;
        }

        return Collections.EMPTY_LIST;
    }

    private boolean destinationReached(String destinationTopicId) {
        return destinationTopicId.equals(clusterMemberId);
    }

    private void createWorkUnit(Message message, String globalEndpointId) {
        Endpoint ep = getMessageHandler(globalEndpointId);

        workUnitProcessor.createWorkUnit(message, ep);
    }

    private Endpoint getMessageHandler(String globalEndpointId) {
        Endpoint ep = localEndpointRegistrations.get(globalEndpointId);

        if (ep == null) {
            throw new IllegalStateException("Cluster member[" +
                    clusterMemberId +
                    "] is unaware of globalEndpointId[" +
                    globalEndpointId +
                    "]");
        }

        return ep;
    }

    private static String getClusterMemberId(String globalEndpointId) {
        return globalEndpointId.split(ID_DELIMITER)[0];
    }

    @Override
    public boolean slotExists(String dataSlotId) {
        throw new UnsupportedOperationException();
//        return clusteredDataSlots.containsKey(dataSlotId);
    }

    @Override
    public boolean endpointRegistered(String dataSlotId, Endpoint endpoint) {
        throw new UnsupportedOperationException("endpointRegistered(...) no supported by " + this.getClass());
    }
}
