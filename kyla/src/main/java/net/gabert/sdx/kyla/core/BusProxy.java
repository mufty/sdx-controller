package net.gabert.sdx.kyla.core;

import net.gabert.sdx.kyla.api.*;

import net.gabert.sdx.kyla.api.Endpoint.Message;
import net.gabert.sdx.kyla.api.DataSlotRegistryProvider;
import net.gabert.sdx.kyla.configuration.KylaConfiguration;
import net.gabert.util.JsonTransformation;
import net.gabert.util.LogUtil;
import org.apache.log4j.Logger;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author Robert Gallas
 */
public class BusProxy implements Bus {
    private static final Logger LOGGER = LogUtil.getLogger();

    private final ExecutorService executorService;

    private final DataSlotRegistryProvider dataSlotRegistryProvider;

    private BusProxy(KylaConfiguration config) {
        this.executorService = Executors.newFixedThreadPool(config.workersCount);

        this.dataSlotRegistryProvider = new DataSlotRegistryProviderProxy(config.dataSlotProviderClassName);

        try {
            registerMBean();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        LOGGER.info("Dataslot provider: " + this.dataSlotRegistryProvider.getClass().getSimpleName());

        LOGGER.info(BusProxy.class.getSimpleName() + " created.");
    }

    private void registerMBean() throws Exception {
        ObjectName objectName = new ObjectName("net.gabert.sdx.kyla:type=BusMonitor");
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        server.registerMBean(new BusMonitor(), objectName);
    }

    public void shutdown() {
        try {
            executorService.shutdown();
            executorService.awaitTermination(10, TimeUnit.SECONDS);
            dataSlotRegistryProvider.shutdown();

            MBeanServer server = ManagementFactory.getPlatformMBeanServer();
            server.unregisterMBean(new ObjectName("net.gabert.sdx.kyla:type=BusMonitor"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void register(Endpoint endpoint) {
        dataSlotRegistryProvider.register(endpoint);
    }

    public void registerExclusive(Endpoint endpoint, String dataSlotId) {
        LOGGER.info("Requested exclusive Endpoint registration: " + endpoint + " -> "+ dataSlotId);
        dataSlotRegistryProvider.registerExclusive(endpoint, dataSlotId);
    }

    public void register(Endpoint endpoint, String dataSlotId) {
        dataSlotRegistryProvider.register(endpoint, dataSlotId);
    }

    public void send(Message message) {
        for ( Endpoint ep : dataSlotRegistryProvider.getEndpoints(message.getDestinationSlotId()) ) {
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

    private class BusMonitor implements BusMonitorMBean {
        @Override
        public List<String> queryDataSlotId(final String dataSlotId) {
            return new ArrayList<String>() {{
                for(Endpoint ep : dataSlotRegistryProvider.getEndpoints(dataSlotId)) {
                    add(ep.getDataSlotId());
                }
            }};
        }
    }

    public static interface BusMonitorMBean {
        List<String> queryDataSlotId(String dataSlotId);
    }
}
