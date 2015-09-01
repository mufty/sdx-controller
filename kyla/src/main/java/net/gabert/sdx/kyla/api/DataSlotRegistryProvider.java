package net.gabert.sdx.kyla.api;

import java.util.List;

/**
 *
 * @author Robert Gallas
 */
public interface DataSlotRegistryProvider {
    void register(Endpoint endpoint);

    void registerExclusive(Endpoint endpoint, String dataSlotId);

    void register(Endpoint endpoint, String dataSlotId);

    boolean slotExists(String dataSlotId);

    List<Endpoint> getEndpoints(String dataSlotId);

    void shutdown();
}
