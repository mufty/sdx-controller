package net.gabert.kyla.configuration;

/**
 * Interface representing Bus configuration options
 */
public class KylaConfiguration {
    /**
     * number of working threads
     */
    public int workersCount;

    /**
     * name of the dataslot provider class name which will be instantiated
     */
    public String dataSlotProviderClassName;
}
