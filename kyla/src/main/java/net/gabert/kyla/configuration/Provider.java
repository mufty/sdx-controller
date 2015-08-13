package net.gabert.kyla.configuration;

import net.gabert.kyla.dataslot.hazelcast.HazelcastDataSlotProvider;
import net.gabert.kyla.dataslot.local.LocalDataSlotProvider;

public enum Provider {
    LOCAL(LocalDataSlotProvider.class.getName()),
    HAZELCAST(HazelcastDataSlotProvider.class.getName());

    public final String providerClassName;

    Provider(String providerClassName) {
        this.providerClassName = providerClassName;
    }
}

