package net.gabert.kyla.dataslot.local;

import net.gabert.kyla.api.Endpoint;
import net.gabert.kyla.api.Endpoint.Message;
import net.gabert.kyla.dataslot.WorkUnitProcessor;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class SharedDataSlot extends DataSlot {
    private final CopyOnWriteArrayList<Endpoint> endpoints = new CopyOnWriteArrayList<>();

    public SharedDataSlot(String dataSlotID, WorkUnitProcessor workUnitProcessor) {
        super(dataSlotID, workUnitProcessor);
    }

    @Override
    public void distribute(Message message) {
        Iterator<Endpoint> iteratorEndpoints = endpoints.iterator();
        while(iteratorEndpoints.hasNext()) {
            Endpoint endpoint = iteratorEndpoints.next();
            workUnitProcessor.createWorkUnit(message, endpoint);
        }
    }

    @Override
    public void register(Endpoint endpoint) {
        endpoints.addIfAbsent(endpoint);
    }

    @Override
    public boolean contains(Endpoint endpoint) {
        return endpoints.contains(endpoint);
    }

    @Override
    public String toString() {
        return "SubscriptionKey["+ getDataSlotId()+"], Subscribers["+endpoints+"]";
    }
}
