package net.gabert.kyla.configuration;

import net.gabert.kyla.api.Configuration;

public class DefaultConfiguration implements Configuration {
    //DEFAULT VALUES
    private int workersCount = 10;
    @Override
    public int getWorkersCount() { return workersCount; }
    public void setWorkersCount(int workersCount) {
        this.workersCount = workersCount; 
    }

    int workerQueueHardLimit = 100;
    @Override
    public int getWorkQueueHardLimit() { 
        return workerQueueHardLimit; 
    }

    private String dataSlotProviderClassName = Provider.LOCAL.providerClassName;
    @Override
    public String getDataSlotProviderClassName() {
        return dataSlotProviderClassName;
    }

    public void setDataSlotProviderClassName(String dataSlotProviderClassName) {
        this.dataSlotProviderClassName = dataSlotProviderClassName;
    }

    public void setDataSlotProvider(Class<?> dataSlotProviderClass) {
        this.dataSlotProviderClassName = dataSlotProviderClass.getName();
    }

    public void setWorkerQueueHardLimit(int workerQueueHardLimit) {
        this.workerQueueHardLimit = workerQueueHardLimit;
    }
}
