package net.gabert.kyla.dataslot.local;

import net.gabert.kyla.api.Endpoint;
import net.gabert.kyla.configuration.KylaConfiguration;
import net.gabert.kyla.test.stub.BusProxyStub;
import net.gabert.kyla.test.stub.EndpointStub;
import net.gabert.util.JsonTransformation;
import org.junit.*;
import static org.junit.Assert.*;

public class EndpointRegistration {
    private BusProxyStub bus;

    @Before
    public void setUp() {
        KylaConfiguration cfg = new JsonTransformation<KylaConfiguration>().fromFile("classpath:kylacfg.json",
                                                                                     KylaConfiguration.class);
        bus = new BusProxyStub(cfg);
    }
    
    @After
    public void tearDown() {
        bus.shutdown();
    }
    
    @Test
    public void singleEndpointRegistration() {
        Endpoint ep1 = new EndpointStub(bus);

        bus.register(ep1);

        assertTrue(bus.isEndpointRegistered(ep1));
    }

    @Test
    public void multipleEndpointsRegistration() {
        Endpoint ep1 = new EndpointStub(bus);
        Endpoint ep2 = new EndpointStub(bus);

        bus.register(ep1);
        bus.register(ep2);

        assertTrue(bus.isEndpointRegistered(ep1));
        assertTrue(bus.isEndpointRegistered(ep2));
    }

    @Test
    public void registerEndpointWithMultipleDataSlots() {
        String dataSlotKey1 = "DATASLOT_1";
        String dataSlotKey2 = "DATASLOT_2";
        String dataSlotKey3 = "DATASLOT_3";

        Endpoint ep1 = new EndpointStub(bus);

        bus.register(ep1, dataSlotKey1);
        bus.register(ep1, dataSlotKey2);
        bus.register(ep1, dataSlotKey3);

        assertTrue(bus.isEndpointRegistered(ep1, dataSlotKey1));
        assertTrue(bus.isEndpointRegistered(ep1, dataSlotKey2));
        assertTrue(bus.isEndpointRegistered(ep1, dataSlotKey3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void exclusiveRegistrationWithClassifierPrefix() {
        Endpoint ep1 = new EndpointStub(bus);

        bus.registerExclusive(ep1, Endpoint.ID_CLASSIFIER + "EP1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void registrationWithClassifierPrefix() {
        Endpoint ep1 = new EndpointStub(bus);

        bus.register(ep1, Endpoint.ID_CLASSIFIER + "EP1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void multipleExclusiveRegistrations() {
        String dataSlotKey1 = "EXCLUSIVE_DATASLOT";

        Endpoint ep1 = new EndpointStub(bus);

        bus.registerExclusive(ep1, dataSlotKey1);
        bus.registerExclusive(ep1, dataSlotKey1);
    }
}
