package net.gabert.kyla.test.stub;

import net.gabert.kyla.api.Endpoint;
import net.gabert.kyla.bus.BusProxy;
import net.gabert.kyla.util.Security;

public class EndpointStub extends Endpoint {
    private Message receivedMessage;

    public EndpointStub(BusProxy busProxy) {
        super(busProxy);
    }

    @Override
    public void handle(Message message) {
        this.receivedMessage = message;
        System.out.println(this.receivedMessage.getData());
    }

    public void sendMessage(String destinationKey, Object messageData) {
        Message msg = createMessage (destinationKey, messageData);
        super.send(msg);
    }

    public Message getReceivedMessage() {
        return receivedMessage;
    }
}
