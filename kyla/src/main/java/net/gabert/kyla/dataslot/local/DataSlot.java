package net.gabert.kyla.dataslot.local;

import net.gabert.kyla.api.Endpoint;
import net.gabert.kyla.dataslot.WorkUnitProcessor;

public abstract class DataSlot {
    private final String dataSlotId;
    protected final WorkUnitProcessor workUnitProcessor;

    protected DataSlot(String dataSlotId, WorkUnitProcessor workUnitProcessor) {
        this.dataSlotId = dataSlotId;
        this.workUnitProcessor = workUnitProcessor;
    }

    public String getDataSlotId() {
        return dataSlotId;
    }

    public abstract void register(Endpoint endpoint);
    
    public abstract boolean contains(Endpoint endpoint);

    public abstract void distribute(Endpoint.Message message);
}
