package net.gabert.sdx.kyla.dataslot.local;

import net.gabert.sdx.kyla.api.Endpoint;
import net.gabert.sdx.kyla.core.BusProxy;
import net.gabert.sdx.kyla.stub.EndpointStub;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Robert Gallas
 */
public class MessageSend {
    private BusProxy bus;

    @Before
    public void setUp() {
        bus = BusProxy.start("classpath:kylacfg.json");
    }

    @Test
    public void exclusiveMessageSend() {
        String message = "EXCLUSIVE_MESSAGE";

        EndpointStub ep1 = new EndpointStub(bus);
        EndpointStub ep2 = new EndpointStub(bus);
        EndpointStub ep3 = new EndpointStub(bus);

        bus.register(ep1);
        bus.register(ep2);
        bus.register(ep3);

        ep1.sendMessage(ep3.getDataSlotId(), message);

        bus.shutdown();

        assertReceivedMessagesSize(0, ep1);
        assertReceivedMessagesSize(0, ep2);
        assertReceivedMessagesSize(1, ep3);

        assertDelivery(message, ep3.getDataSlotId(), ep1, ep3);
    }

    @Test
    public void exclusiveMessageSend2() {
        String message = "EXCLUSIVE_MESSAGE";

        EndpointStub ep1 = new EndpointStub(bus);
        EndpointStub ep2 = new EndpointStub(bus);
        EndpointStub ep3 = new EndpointStub(bus);

        bus.register(ep1);
        bus.registerExclusive(ep2, "DATA_SLOT_2");
        bus.registerExclusive(ep3, "DATA_SLOT_3");

        ep1.sendMessage("DATA_SLOT_3", message);

        bus.shutdown();

        assertReceivedMessagesSize(0, ep1);
        assertReceivedMessagesSize(0, ep2);
        assertReceivedMessagesSize(1, ep3);

        assertDelivery(message, "DATA_SLOT_3", ep1, ep3);
    }

    @Test
    public void sharedMessageSend() {
        String message = "SHARED_MESSAGE";

        EndpointStub ep1 = new EndpointStub(bus);
        EndpointStub ep2 = new EndpointStub(bus);
        EndpointStub ep3 = new EndpointStub(bus);

        bus.register(ep1);
        bus.registerShared(ep2, "SHARED_DATA_SLOT");
        bus.registerShared(ep3, "SHARED_DATA_SLOT");

        ep1.sendMessage("SHARED_DATA_SLOT", message);

        bus.shutdown();

        assertReceivedMessagesSize(0, ep1);
        assertReceivedMessagesSize(1, ep2);
        assertReceivedMessagesSize(1, ep3);

        assertDelivery(message, "SHARED_DATA_SLOT", ep1, ep2);
        assertDelivery(message, "SHARED_DATA_SLOT", ep1, ep3);
    }

    private static void assertReceivedMessagesSize(int expectedSize, EndpointStub ep) {
        assertEquals(expectedSize, ep.getReceivedMessages().size());
    }

    private static void assertDelivery(String message, String dataSlotId, EndpointStub source, EndpointStub destination) {
        Endpoint.Message receivedMessage = destination.getReceivedMessages().get(0);
        assertEquals(message, receivedMessage.getData());
        assertEquals(dataSlotId, receivedMessage.getDestinationSlotId());
        assertEquals(source.getDataSlotId(), receivedMessage.getSourceSlotId());
    }
}
