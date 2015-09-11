package net.gabert.sdx.kyla.api;

import net.gabert.sdx.kyla.api.Endpoint;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Robert Gallas
 */
public abstract class DataSlot {
    private final CopyOnWriteArrayList<Endpoint> endpoints = new CopyOnWriteArrayList<>();

    private final String dataSlotId;

    protected DataSlot(String dataSlotId) {
        this.dataSlotId = dataSlotId;
    }

    protected String getDataSlotId() {
        return dataSlotId;
    }

    public List<Endpoint> getEndpoints() {
        return endpoints;
    }

    public void register(Endpoint endpoint) {
        endpoints.addIfAbsent(endpoint);
    }

    @Override
    public String toString() {
        return "SubscriptionKey[" + getDataSlotId() + "]";
    }
}
