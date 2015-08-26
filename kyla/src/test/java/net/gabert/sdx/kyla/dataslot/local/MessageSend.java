package net.gabert.sdx.kyla.dataslot.local;

import net.gabert.sdx.kyla.configuration.KylaConfiguration;
import net.gabert.sdx.kyla.core.BusProxy;
import net.gabert.sdx.kyla.stub.EndpointStub;
import net.gabert.util.JsonTransformation;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MessageSend {
    private BusProxy bus;

    @Before
    public void setUp() {
        KylaConfiguration cfg = new JsonTransformation<KylaConfiguration>().fromFile("classpath:kylacfg.json",
                                                                                     KylaConfiguration.class);
        bus = BusProxy.start(cfg);
    }

    @Test
    public void exclusiveMessageSend() {
        String exclusiveMessage = "EXCLUSIVE_MESSAGE";

        EndpointStub ep1 = new EndpointStub(bus);
        EndpointStub ep2 = new EndpointStub(bus);

        bus.register(ep1);
        bus.register(ep2);

        ep1.sendMessage(ep2.getDataSlotId(), exclusiveMessage);

        bus.shutdown();

        assertEquals(exclusiveMessage, ep2.getReceivedMessage().getData());
        assertEquals(ep2.getDataSlotId(), ep2.getReceivedMessage().getDestinationSlotId());
        assertEquals(ep1.getDataSlotId(), ep2.getReceivedMessage().getSourceSlotId());
        assertNull(ep1.getReceivedMessage());
    }
}
