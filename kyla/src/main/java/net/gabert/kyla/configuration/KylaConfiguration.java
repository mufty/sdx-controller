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
     * if number of work units / messages present in bus working exceeds Work queue Hard Limit, sending messages
     * to bus becomes synchronous operation.
     */
    public int workQueueHardLimit;

    /**
     * name of the dataslot provider class name which will be instantiated
     */
    public String dataSlotProviderClassName;
}
