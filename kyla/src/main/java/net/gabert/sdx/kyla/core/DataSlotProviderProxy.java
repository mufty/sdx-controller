package net.gabert.sdx.kyla.core;

import net.gabert.sdx.kyla.api.DataSlotProvider;
import net.gabert.sdx.kyla.api.Endpoint;

import java.lang.reflect.Constructor;
import java.util.List;

public class DataSlotProviderProxy implements DataSlotProvider {
    private final DataSlotProvider provider;

    public DataSlotProviderProxy(String dataSlotProviderClassName) {
        this.provider = loadProvider(dataSlotProviderClassName);
    }

    private static DataSlotProvider loadProvider(String dataSlotProviderClassName) {
        try {
            Class<?> clazz = Class.forName(dataSlotProviderClassName);

            Constructor<?> constructor = clazz.getConstructor();
            DataSlotProvider dataSlotProvider = (DataSlotProvider)constructor.newInstance();

            return dataSlotProvider;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void register(Endpoint endpoint) {
        String dataSlotId = endpoint.getDataSlotId();
        if (dataSlotId.startsWith(Endpoint.ID_CLASSIFIER) == false) {
            throw new IllegalArgumentException("Endpoint registration dataSlotId must contain " +
                                               "Endpoint.ID_CLASSIFIER");
        }

        provider.register(endpoint);
    }

    @Override
    public void registerExclusive(Endpoint endpoint, String dataSlotId) {
        if (dataSlotId.startsWith(Endpoint.ID_CLASSIFIER)) {
            throw new IllegalArgumentException("Exclusive registration with dataSlotId starting with endpoint " +
                                               "classifier not allowed");
        }

        provider.registerExclusive(endpoint, dataSlotId);
    }

    @Override
    public void register(Endpoint endpoint, String dataSlotId) {
        if (dataSlotId.startsWith(Endpoint.ID_CLASSIFIER)) {
            throw new IllegalArgumentException("Registration with dataSlotId starting with endpoint " +
                                               "classifier not allowed");
        }

        provider.register(endpoint, dataSlotId);
    }

    @Override
    public boolean slotExists(String dataSlotId) {
        return provider.slotExists(dataSlotId);
    }

    @Override
    public List<Endpoint> getEndpoints(String dataSlotId) {
        return provider.getEndpoints(dataSlotId);
    }

    @Override
    public void shutdown() {
        provider.shutdown();
    }
}
