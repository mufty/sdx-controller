package net.gabert.kyla.api;

import net.gabert.kyla.api.Endpoint.Message;

import java.util.List;

public interface DataSlotProvider {
    void register(Endpoint endpoint);

    void registerExclusive(Endpoint endpoint, String dataSlotId);

    void register(Endpoint endpoint, String dataSlotId);

    boolean slotExists(String dataSlotId);

    List<Endpoint> getEndpoints(String dataSlotId);

    void shutdown();
}
