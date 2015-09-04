package net.gabert.sdx.kyla.core;

import net.gabert.sdx.kyla.api.DataSlotRegistryProvider;
import net.gabert.sdx.kyla.api.Endpoint;
import net.gabert.util.LogUtil;
import org.slf4j.Logger;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 *
 * @author Robert Gallas
 */
public class DataSlotRegistryProviderProxy implements DataSlotRegistryProvider {
    private static final Logger LOGGER = LogUtil.getLogger();

    private final DataSlotRegistryProvider provider;

    public DataSlotRegistryProviderProxy(String dataSlotProviderClassName) {
        this.provider = loadProvider(dataSlotProviderClassName);
        LOGGER.info("Dataslot provider: {}", this.provider.getClass().getSimpleName());
    }

    private static DataSlotRegistryProvider loadProvider(String dataSlotProviderClassName) {
        try {
            Class<?> clazz = Class.forName(dataSlotProviderClassName);

            Constructor<?> constructor = clazz.getConstructor();

            return (DataSlotRegistryProvider)constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void register(Endpoint endpoint) {
        String dataSlotId = endpoint.getDataSlotId();
        if (dataSlotId.startsWith(Endpoint.ID_CLASSIFIER) == false) {
            throw new IllegalArgumentException("Endpoint dataSlotId must start with " +
                                               Endpoint.ID_CLASSIFIER);
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
    public void registerShared(Endpoint endpoint, String dataSlotId) {
        if (dataSlotId.startsWith(Endpoint.ID_CLASSIFIER)) {
            throw new IllegalArgumentException("Shared registration with dataSlotId starting with endpoint " +
                                               "classifier not allowed");
        }

        provider.registerShared(endpoint, dataSlotId);
    }

    @Override
    public void registerParallel(Endpoint endpoint, String dataSlotId) {
        if (dataSlotId.startsWith(Endpoint.ID_CLASSIFIER)) {
            throw new IllegalArgumentException("Parallel registration with dataSlotId starting with endpoint " +
                                               "classifier not allowed");
        }

        provider.registerParallel(endpoint, dataSlotId);
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
