package net.gabert.sdx.kyla.stub;

import net.gabert.sdx.kyla.api.Endpoint;
import net.gabert.sdx.kyla.core.BusProxy;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Robert Gallas
 */
public class EndpointStub extends Endpoint {
    private List<Message> receivedMessage = new ArrayList<>();

    public EndpointStub(BusProxy busProxy) {
        super(busProxy);
    }

    @Override
    public void handle(Message message) {
        this.receivedMessage.add(message);
    }

    public void sendMessage(String destinationKey, Object messageData) {
        Message msg = createMessage (destinationKey, messageData);
        super.send(msg);
    }

    public List<Message> getReceivedMessages() {
        return receivedMessage;
    }
}
