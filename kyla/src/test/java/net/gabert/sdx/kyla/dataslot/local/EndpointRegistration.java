package net.gabert.sdx.kyla.dataslot.local;

import net.gabert.sdx.kyla.api.Endpoint;
import net.gabert.sdx.kyla.configuration.KylaConfiguration;
import net.gabert.sdx.kyla.core.BusProxy;
import net.gabert.sdx.kyla.stub.EndpointStub;
import net.gabert.util.JsonTransformation;
import org.junit.*;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 * @author Robert Gallas
 */
public class EndpointRegistration {
    private BusProxy bus;
    private MBeanServer server = ManagementFactory.getPlatformMBeanServer();

    @Before
    public void setUp() {
        bus = BusProxy.start("classpath:kylacfg.json");
    }
    
    @After
    public void tearDown() {
        bus.shutdown();
    }
    
    @Test
    public void singleEndpointRegistration() {
        Endpoint ep1 = new EndpointStub(bus);

        bus.register(ep1);

        assertTrue(isEndpointRegistered(ep1));
    }

    @Test
    public void multipleEndpointsRegistration() {
        Endpoint ep1 = new EndpointStub(bus);
        Endpoint ep2 = new EndpointStub(bus);

        bus.register(ep1);
        bus.register(ep2);

        assertTrue(isEndpointRegistered(ep1));
        assertTrue(isEndpointRegistered(ep2));
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

        assertTrue(isEndpointRegistered(ep1, dataSlotKey1));
        assertTrue(isEndpointRegistered(ep1, dataSlotKey2));
        assertTrue(isEndpointRegistered(ep1, dataSlotKey3));
    }

    public boolean isEndpointRegistered(Endpoint endpoint) {
        return  isEndpointRegistered(endpoint, endpoint.getDataSlotId());
    }

    public boolean isEndpointRegistered(Endpoint endpoint, String dataSlotId) {
        return  isEndpointRegistered(endpoint.getDataSlotId(), dataSlotId)   ;
    }

    public boolean isEndpointRegistered(String endpointId, String dataSlotId) {
        try {
            List<String> endpointIds =
                    (List<String>)server.invoke(new ObjectName("net.gabert.sdx.kyla:type=BusMonitor"),
                                                "queryDataSlotId",
                                                new String[]{dataSlotId},
                                                new String[] {dataSlotId.getClass().getCanonicalName()});
            return endpointIds.contains(endpointId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
