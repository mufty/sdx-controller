package net.gabert.sdx.kyla.stub;

import net.gabert.sdx.kyla.api.Endpoint;
import net.gabert.sdx.kyla.core.BusProxy;

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
