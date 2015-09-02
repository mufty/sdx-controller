package net.gabert.sdx.kyla.dataslot.local;

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

        assertNull(ep1.getReceivedMessage());
        assertNull(ep2.getReceivedMessage());
        assertNotNull(ep3.getReceivedMessage());

        assertEquals(message, ep3.getReceivedMessage().getData());
        assertEquals(ep3.getDataSlotId(), ep3.getReceivedMessage().getDestinationSlotId());
        assertEquals(ep1.getDataSlotId(), ep3.getReceivedMessage().getSourceSlotId());
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

        assertNull(ep1.getReceivedMessage());
        assertNull(ep2.getReceivedMessage());
        assertNotNull(ep3.getReceivedMessage());

        assertEquals(message, ep3.getReceivedMessage().getData());
        assertEquals("DATA_SLOT_3", ep3.getReceivedMessage().getDestinationSlotId());
        assertEquals(ep1.getDataSlotId(), ep3.getReceivedMessage().getSourceSlotId());
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

        assertNull(ep1.getReceivedMessage());
        assertNotNull(ep2.getReceivedMessage());
        assertNotNull(ep3.getReceivedMessage());

        assertEquals(message, ep2.getReceivedMessage().getData());
        assertEquals(message, ep3.getReceivedMessage().getData());
        assertEquals("SHARED_DATA_SLOT", ep2.getReceivedMessage().getDestinationSlotId());
        assertEquals(ep1.getDataSlotId(), ep2.getReceivedMessage().getSourceSlotId());
        assertEquals("SHARED_DATA_SLOT", ep3.getReceivedMessage().getDestinationSlotId());
        assertEquals(ep1.getDataSlotId(), ep3.getReceivedMessage().getSourceSlotId());
    }
}
