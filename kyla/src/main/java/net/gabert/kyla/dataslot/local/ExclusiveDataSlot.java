package net.gabert.kyla.dataslot.local;

import net.gabert.kyla.api.Endpoint;
import net.gabert.kyla.api.Endpoint.Message;
import net.gabert.kyla.dataslot.WorkUnitProcessor;

public class ExclusiveDataSlot extends DataSlot {
    private final Endpoint endpoint;

    public ExclusiveDataSlot(Endpoint endpoint, String dataSlotID, WorkUnitProcessor workUnitProcessor) {
        super(dataSlotID, workUnitProcessor);
        this.endpoint = endpoint;
    }

    @Override
    public void distribute(Message message) {
        workUnitProcessor.createWorkUnit(message, endpoint);
    }

    @Override
    public void register(Endpoint endpoint) {
        throw new IllegalArgumentException("Registration of ["+endpoint+"] not allowed to ExclusiveDataSlot " + this);
    }

    @Override
    public boolean contains(Endpoint endpoint) {
        return this.endpoint.equals(endpoint);
    }

    @Override
    public String toString() {
        return "SubscriptionKey["+ getDataSlotId()+"], Subscriber["+endpoint+"]";
    }

}
