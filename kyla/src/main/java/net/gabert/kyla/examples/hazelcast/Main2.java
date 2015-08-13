package net.gabert.kyla.examples.hazelcast;

import net.gabert.kyla.bus.BusProxy;
import net.gabert.kyla.configuration.DefaultConfiguration;
import net.gabert.kyla.configuration.Provider;

public class Main2 {
    public static void main(String[] args) {
        DefaultConfiguration cfg = new DefaultConfiguration();
        cfg.setDataSlotProviderClassName(Provider.HAZELCAST.providerClassName);
        BusProxy bus = new BusProxy(cfg);
        bus.start();
        EndpointImpl ep = new EndpointImpl(bus);

        bus.register(ep, "SLOT2");

        ep.sendMessage("Hello WORLD", "SLOT1");
    }
}
