package net.gabert.kyla.configuration;

import net.gabert.kyla.api.KylaConfiguration;
import net.gabert.kyla.dataslot.local.LocalDataSlotProvider;

public class DefaultKylaConfiguration implements KylaConfiguration {
    //DEFAULT VALUES
    private int workersCount = 10;
    @Override
    public int getWorkersCount() { return workersCount; }

    int workerQueueHardLimit = 100;
    @Override
    public int getWorkQueueHardLimit() { 
        return workerQueueHardLimit; 
    }

    private String dataSlotProviderClassName = LocalDataSlotProvider.class.getName();
    @Override
    public String getDataSlotProviderClassName() {
        return dataSlotProviderClassName;
    }
}
