package net.gabert.kyla.dataslot.local;

import net.gabert.kyla.api.*;
import net.gabert.kyla.api.DataSlotProvider;
import net.gabert.kyla.dataslot.WorkUnitProcessor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LocalDataSlotProvider implements DataSlotProvider {
    private final Map<String, DataSlot> localDataSlots;
    private final WorkUnitProcessor workUnitProcessor;

    public LocalDataSlotProvider(WorkUnitProcessor workUnitProcessor) {
        this.localDataSlots = Collections.synchronizedMap(new HashMap<String, DataSlot>());
        this.workUnitProcessor = workUnitProcessor;
    }

    public static DataSlotProvider getInstance(WorkUnitProcessor workUnitProcessor) {
        return new LocalDataSlotProvider(workUnitProcessor);
    }

    @Override
    public void register(Endpoint endpoint) {
        String dataSlotId = endpoint.getDataSlotId();
        if (dataSlotId.startsWith(Endpoint.ID_CLASSIFIER) == false) {
            throw new IllegalArgumentException("Endpoint registration dataSlotId must contain " +
                    "Endpoint.ID_CLASSIFIER");
        }

        exclusiveRegistration(endpoint, dataSlotId);
    }

    @Override
    public void registerExclusive(Endpoint endpoint, String dataSlotId) {
        if (dataSlotId.startsWith(Endpoint.ID_CLASSIFIER)) {
            throw new IllegalArgumentException("Exclusive registration with dataSlotId starting with endpoint " +
                    "classifier not allowed");
        }

        register(endpoint);
        exclusiveRegistration(endpoint, dataSlotId);
    }

    private void exclusiveRegistration(Endpoint endpoint, String dataSlotId) {
        synchronized (localDataSlots) {
            if (localDataSlots.containsKey(dataSlotId) == false) {
                localDataSlots.put(dataSlotId, new ExclusiveDataSlot(endpoint, dataSlotId, workUnitProcessor));
            } else {
                throw new IllegalArgumentException("Cannot make exclusive subscription. DataSlot["
                        + dataSlotId
                        + "] already created ["
                        + localDataSlots.get(dataSlotId)
                        + "]");
            }
        }
    }

    @Override
    public void register(Endpoint endpoint, String dataSlotId) {
        synchronized (localDataSlots) {
            if (dataSlotId.startsWith(Endpoint.ID_CLASSIFIER)) {
                throw new IllegalArgumentException("Registration with dataSlotId starting with endpoint " +
                        "classifier not allowed");
            }

            if (localDataSlots.containsKey(dataSlotId) == false) {
                localDataSlots.put(dataSlotId, new SharedDataSlot(dataSlotId, workUnitProcessor));
            }
        }

        DataSlot dataSlot = localDataSlots.get(dataSlotId);
        dataSlot.register(endpoint);
    }

    @Override
    public void distribute(Endpoint.Message message, String destinationDataSlotId) {
        DataSlot dataSlot = localDataSlots.get(message.getDestinationSlotId());
        dataSlot.distribute(message);
    }

    @Override
    public boolean slotExists(String dataSlotId) {
        return localDataSlots.containsKey(dataSlotId);
    }

    @Override
    public boolean endpointRegistered(String dataSlotId, Endpoint endpoint) {
        return localDataSlots.containsKey(dataSlotId) && localDataSlots.get(dataSlotId).contains(endpoint);
    }

    @Override
    public void shutdown() {
        //TODO: Clear caches
    }
}
