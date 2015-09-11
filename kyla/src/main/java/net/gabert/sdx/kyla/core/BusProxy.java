package net.gabert.sdx.kyla.core;

import net.gabert.sdx.kyla.api.*;

import net.gabert.sdx.kyla.api.Endpoint.Message;
import net.gabert.sdx.kyla.api.DataSlotRegistry;
import net.gabert.sdx.kyla.configuration.KylaConfiguration;
import net.gabert.util.JsonTransformation;
import net.gabert.util.LogUtil;
import org.slf4j.Logger;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Robert Gallas
 */
public class BusProxy implements Bus, DataSlotRegistry {
    private static final Logger LOGGER = LogUtil.getLogger();

    private final ExecutorService executorService;

    private final DataSlotRegistry dataSlotRegistry;

    private BusProxy(KylaConfiguration config) {
        this.executorService = Executors.newFixedThreadPool(config.workersCount);

        this.dataSlotRegistry = initRegistry(config.dataSlotProviderClassName);
        LOGGER.info("Dataslot provider: {}", this.dataSlotRegistry.getClass().getSimpleName());

        try {
            registerMBean();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        LOGGER.info("{} created.", BusProxy.class.getSimpleName());
    }

    private static DataSlotRegistry initRegistry(String dataSlotProviderClassName) {
        try {
            Class<?> clazz = Class.forName(dataSlotProviderClassName);

            Constructor<?> constructor = clazz.getConstructor();

            return (DataSlotRegistry)constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void shutdown() {
        try {
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.SECONDS);
            dataSlotRegistry.shutdown();

            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            server.unregisterMBean(new ObjectName("net.gabert.sdx.kyla:type=BusMonitor"));
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

        dataSlotRegistry.register(endpoint);
    }

    @Override
    public void registerExclusive(Endpoint endpoint, String dataSlotId) {
        LOGGER.info("Requested exclusive Endpoint registration: {} -> {}", endpoint, dataSlotId);

        if (dataSlotId.startsWith(Endpoint.ID_CLASSIFIER)) {
            throw new IllegalArgumentException("Exclusive registration with dataSlotId starting with endpoint " +
                                               "classifier not allowed");
        }

        dataSlotRegistry.registerExclusive(endpoint, dataSlotId);
    }

    @Override
    public void registerShared(Endpoint endpoint, String dataSlotId) {
        if (dataSlotId.startsWith(Endpoint.ID_CLASSIFIER)) {
            throw new IllegalArgumentException("Shared registration with dataSlotId starting with endpoint " +
                                               "classifier not allowed");
        }

        dataSlotRegistry.registerShared(endpoint, dataSlotId);
    }

    @Override
    public void registerParallel(Endpoint endpoint, String dataSlotId) {
        if (dataSlotId.startsWith(Endpoint.ID_CLASSIFIER)) {
            throw new IllegalArgumentException("Parallel registration with dataSlotId starting with endpoint " +
                                               "classifier not allowed");
        }

        dataSlotRegistry.registerParallel(endpoint, dataSlotId);
    }

    @Override
    public boolean slotExists(String dataSlotId) {
        return dataSlotRegistry.slotExists(dataSlotId);
    }

    @Override
    public List<Endpoint> getEndpoints(String dataSlotId) {
        return dataSlotRegistry.getEndpoints(dataSlotId);
    }

    public void send(Message message) {
        for ( Endpoint ep : dataSlotRegistry.getEndpoints(message.getDestinationSlotId()) ) {
            this.executorService.submit(createTask(ep, message));
        }
    }

    private static Runnable createTask(final Endpoint endpoint, final Message message) {
        return new Runnable() {
            @Override
            public void run() {endpoint.handle(message);}
        };
    }

    public static BusProxy start(String kylaConfigUrl) {
        KylaConfiguration kylaCfg = new JsonTransformation<KylaConfiguration>().fromFile(kylaConfigUrl,
                                                                                         KylaConfiguration.class);
        return new BusProxy(kylaCfg);
    }

    private void registerMBean() throws Exception {
        ObjectName objectName = new ObjectName("net.gabert.sdx.kyla:type=BusMonitor");
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        server.registerMBean(new BusMonitor(), objectName);
    }

    private class BusMonitor implements BusMonitorMBean {
        @Override
        public List<String> queryDataSlotId(final String dataSlotId) {
            return new ArrayList<String>() {{
                for(Endpoint ep : dataSlotRegistry.getEndpoints(dataSlotId)) {
                    add(ep.getDataSlotId());
                }
            }};
        }
    }

    public static interface BusMonitorMBean {
        List<String> queryDataSlotId(String dataSlotId);
    }
}
