package net.gabert.kyla.dataslot.local;

import net.gabert.kyla.configuration.DefaultKylaConfiguration;
import net.gabert.kyla.test.stub.BusProxyStub;
import net.gabert.kyla.test.stub.EndpointStub;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MessageSend {
    private BusProxyStub bus;

    @Before
    public void setUp() {
        DefaultKylaConfiguration cfg = new DefaultKylaConfiguration();
        bus = new BusProxyStub(cfg);
        bus.start();
    }

    @After
    public void tearDown() {
        bus.shutdown();
    }

    @Test
    public void exclusiveMessageSend() {
        String exclusiveMessage = "EXCLUSIVE_MESSAGE";

        EndpointStub ep1 = new EndpointStub(bus);
        EndpointStub ep2 = new EndpointStub(bus);

        bus.register(ep1);
        bus.register(ep2);

        ep1.sendMessage(ep2.getDataSlotId(), exclusiveMessage);

        bus.await();

        assertEquals(exclusiveMessage, ep2.getReceivedMessage().getData());
        assertEquals(ep2.getDataSlotId(), ep2.getReceivedMessage().getDestinationSlotId());
        assertEquals(ep1.getDataSlotId(), ep2.getReceivedMessage().getSourceSlotId());
        assertNull(ep1.getReceivedMessage());
    }
}
