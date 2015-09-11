package net.gabert.sdx.kyla.dataslot.local;

import net.gabert.sdx.kyla.api.*;
import net.gabert.sdx.kyla.api.DataSlotRegistry;
import net.gabert.util.LogUtil;
import org.slf4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Robert Gallas
 */
public class LocalDataSlotRegistry implements DataSlotRegistry {
    private static final Logger LOGGER = LogUtil.getLogger();
    private final Map<String, DataSlot> dataSlots = Collections.synchronizedMap(new HashMap<String, DataSlot>());

    @Override
    public void register(Endpoint endpoint) {
        exclusiveRegistration(endpoint, endpoint.getDataSlotId());
    }

    @Override
    public void registerExclusive(Endpoint endpoint, String dataSlotId) {
        exclusiveRegistration(endpoint, dataSlotId);
    }

    private void exclusiveRegistration(Endpoint endpoint, String dataSlotId) {
        synchronized (dataSlots) {
            if (dataSlots.containsKey(dataSlotId) == false) {
                dataSlots.put(dataSlotId, new ExclusiveDataSlot(endpoint, dataSlotId));
            } else {
                throw new IllegalArgumentException("Cannot make exclusive subscription. DataSlot["
                                                   + dataSlotId
                                                   + "] already created ["
                                                   + dataSlots.get(dataSlotId)
                                                   + "]");
            }
        }

        LOGGER.info("Endpoint exclusive registration: {}", dataSlotId);
    }

    @Override
    public void registerShared(Endpoint endpoint, String dataSlotId) {
        synchronized (dataSlots) {
            if (dataSlots.containsKey(dataSlotId) == false) {
                dataSlots.put(dataSlotId, new SharedDataSlot(dataSlotId));
            }
        }

        DataSlot dataSlot = dataSlots.get(dataSlotId);
        dataSlot.register(endpoint);
    }

    @Override
    public void registerParallel(Endpoint endpoint, String dataSlotId) {
        synchronized (dataSlots) {
            if (dataSlots.containsKey(dataSlotId) == false) {
                dataSlots.put(dataSlotId, new ParallelDataSlot(dataSlotId));
            }
        }

        DataSlot dataSlot = dataSlots.get(dataSlotId);
        dataSlot.register(endpoint);
    }

    @Override
    public boolean slotExists(String dataSlotId) {
        return dataSlots.containsKey(dataSlotId);
    }

    @Override
    public List<Endpoint> getEndpoints(String dataSlotId) {
        return dataSlots.get(dataSlotId).getEndpoints();
    }

    @Override
    public void shutdown() {
        //TODO: Clear caches
    }
}
