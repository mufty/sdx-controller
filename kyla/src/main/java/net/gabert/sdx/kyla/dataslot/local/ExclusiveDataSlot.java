package net.gabert.sdx.kyla.dataslot.local;

import net.gabert.sdx.kyla.api.Endpoint;

public class ExclusiveDataSlot extends DataSlot {

    public ExclusiveDataSlot(Endpoint endpoint, String dataSlotID) {
        super(dataSlotID);
        endpoints.addIfAbsent(endpoint);
    }

    @Override
    public void register(Endpoint endpoint) {
        throw new IllegalArgumentException("Registration of ["+endpoint+"] not allowed to ExclusiveDataSlot " + this);
    }

    @Override
    public String toString() {
        return "SubscriptionKey["+ getDataSlotId()+"], Subscriber["+ endpoints.get(0)+"]";
    }

}
