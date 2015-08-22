package net.gabert.kyla.api;

/**
 * Interface representing Bus configuration options
 */
public interface KylaConfiguration {
    /**
     * @return number of working threads
     */
    int getWorkersCount();

    /**
     * @return if number of work units / messages present in bus working exceeds Work queue Hard Limit, sending messages
     * to bus becomes synchronous operation.
     */
    int getWorkQueueHardLimit();

    /**
     * @return name of the dataslot provider class name which will be instantiated
     */
    String getDataSlotProviderClassName();
}
