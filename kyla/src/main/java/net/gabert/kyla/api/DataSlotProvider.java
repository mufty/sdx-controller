package net.gabert.kyla.api;

import net.gabert.kyla.api.Endpoint.Message;

public interface DataSlotProvider {
    void register(Endpoint endpoint);

    void registerExclusive(Endpoint endpoint, String dataSlotId);

    void register(Endpoint endpoint, String dataSlotId);

    void distribute(Message message, String destinationDataSlotId);

    boolean slotExists(String dataSlotId);

    boolean endpointRegistered(String dataSlotId, Endpoint endpoint);

    void shutdown();
}
