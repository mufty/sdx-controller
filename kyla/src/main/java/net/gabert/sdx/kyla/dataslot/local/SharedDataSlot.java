package net.gabert.sdx.kyla.dataslot.local;

import net.gabert.sdx.kyla.api.Endpoint;

public class SharedDataSlot extends DataSlot {

    public SharedDataSlot(String dataSlotID) {
        super(dataSlotID);
    }

    @Override
    public void register(Endpoint endpoint) {
        endpoints.addIfAbsent(endpoint);
    }

    @Override
    public String toString() {
        return "SubscriptionKey["+ getDataSlotId()+"], Subscribers["+endpoints+"]";
    }
}
