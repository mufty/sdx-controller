package net.gabert.kyla.dataslot.local;

import net.gabert.kyla.api.Endpoint;
import java.util.concurrent.CopyOnWriteArrayList;

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
