package net.gabert.sdx.kyla.dataslot.local;

import net.gabert.sdx.kyla.api.Endpoint;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Robert Gallas
 */
public abstract class DataSlot {
    protected final CopyOnWriteArrayList<Endpoint> endpoints = new CopyOnWriteArrayList<>();

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

    public abstract void register(Endpoint endpoint);
}
