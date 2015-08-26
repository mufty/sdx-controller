package net.gabert.kyla.core;

import net.gabert.kyla.api.*;

import net.gabert.kyla.api.Endpoint.Message;
import net.gabert.kyla.api.DataSlotProvider;
import net.gabert.kyla.configuration.KylaConfiguration;
import net.gabert.util.LogUtil;
import org.apache.log4j.Logger;

import javax.management.*;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 
 * @author Robert Gallas
 */
public class BusProxy implements Bus {
    private static final Logger LOGGER = LogUtil.getLogger();

    private final ExecutorService executorService;

    private final DataSlotProvider dataSlotProvider;

    private BusProxy(KylaConfiguration config) {
        this.executorService = Executors.newFixedThreadPool(config.workersCount);

        this.dataSlotProvider = new DataSlotProviderProxy(config.dataSlotProviderClassName);

        try {
            registerMBean();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        LOGGER.info("Dataslot provider: " + this.dataSlotProvider.getClass().getSimpleName());

        LOGGER.info(BusProxy.class.getSimpleName() + " created.");
    }

    private void registerMBean() throws Exception {
        ObjectName objectName = new ObjectName("net.gabert.sdx.kyla:type=BusMonitor");
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        server.registerMBean(new BusMonitor(), objectName);
    }

    public void shutdown() {
        executorService.shutdown();
        dataSlotProvider.shutdown();
        MBeanServer server = ManagementFactory.getPlatformMBeanServer();
        try {
            server.unregisterMBean(new ObjectName("net.gabert.sdx.kyla:type=BusMonitor"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void register(Endpoint endpoint) {
        dataSlotProvider.register(endpoint);
    }

    public void registerExclusive(Endpoint endpoint, String dataSlotId) {
        LOGGER.info("Requested exclusive Endpoint registration: " + endpoint + " -> "+ dataSlotId);
        dataSlotProvider.registerExclusive(endpoint, dataSlotId);
    }

    public void register(Endpoint endpoint, String dataSlotId) {
        dataSlotProvider.register(endpoint, dataSlotId);
    }

    public void send(Message message) {
        for ( Endpoint ep : dataSlotProvider.getEndpoints(message.getDestinationSlotId()) ) {
            this.executorService.submit(createTask(ep, message));
        }
    }

    private static Runnable createTask(final Endpoint endpoint, final Message message) {
        return new Runnable() {
            @Override
            public void run() {endpoint.handle(message);}
        };
    }

    public static BusProxy start(KylaConfiguration kylaCfg) {
        return new BusProxy(kylaCfg);
    }

    private class BusMonitor implements BusMonitorMBean {
        @Override
        public List<String> queryDataSlotId(final String dataSlotId) {
            return new ArrayList<String>() {{
                for(Endpoint ep : dataSlotProvider.getEndpoints(dataSlotId)) {
                    add(ep.getDataSlotId());
                }
            }};
        }
    }

    public static interface BusMonitorMBean {
        List<String> queryDataSlotId(String dataSlotId);
    }
}
