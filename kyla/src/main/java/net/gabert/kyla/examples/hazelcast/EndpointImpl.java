package net.gabert.kyla.examples.hazelcast;

import net.gabert.kyla.api.Endpoint;
import net.gabert.kyla.bus.BusProxy;

public class EndpointImpl extends Endpoint {
    protected EndpointImpl(BusProxy busProxy) {
        super(busProxy);
    }

    @Override
    public void handle(Message message) {
        System.out.println(message + " : " + super.getDataSlotId());
    }

    public void sendMessage(Object message, String dataSlotID) {
        send(createMessage(dataSlotID, message));
    }
}
