package net.gabert.kyla.dataslot.local;

import net.gabert.kyla.api.Endpoint;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
