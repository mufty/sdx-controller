package net.gabert.kyla.examples.hazelcast;

import net.gabert.kyla.api.Configuration;
import net.gabert.kyla.api.Endpoint;
import net.gabert.kyla.bus.BusProxy;
import net.gabert.kyla.configuration.DefaultConfiguration;
import net.gabert.kyla.configuration.Provider;

public class Main1 {
    public static void main(String[] args) {
        BusProxy bus = new BusProxy(getConfig());
        bus.start();

        Endpoint ep = new EndpointImpl(bus);

        bus.register(ep, "SLOT1");
    }

    private static Configuration getConfig() {
        DefaultConfiguration cfg = new DefaultConfiguration();
        cfg.setDataSlotProviderClassName(Provider.HAZELCAST.providerClassName);

        return cfg;
    }
}
