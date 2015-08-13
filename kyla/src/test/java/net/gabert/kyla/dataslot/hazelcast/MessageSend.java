package net.gabert.kyla.dataslot.hazelcast;

import net.gabert.kyla.configuration.DefaultConfiguration;
import net.gabert.kyla.configuration.Provider;
import net.gabert.kyla.test.stub.BusProxyStub;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class MessageSend {
    private BusProxyStub bus1;
    private BusProxyStub bus2;

    @Before
    public void setUp() {
        DefaultConfiguration cfg = new DefaultConfiguration();
        cfg.setDataSlotProviderClassName(Provider.HAZELCAST.providerClassName);
        bus1 = new BusProxyStub(cfg);
        bus1.start();

        bus2 = new BusProxyStub(cfg);
        bus2.start();
    }

    @After
    public void tearDown() {
        bus1.shutdown();
        bus2.shutdown();
    }

    @Test
    public void exclusiveMessageSend() {
    }
}
