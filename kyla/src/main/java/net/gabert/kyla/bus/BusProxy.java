package net.gabert.kyla.bus;

import net.gabert.kyla.api.*;

import net.gabert.kyla.api.Endpoint.Message;
import net.gabert.kyla.api.DataSlotProvider;
import net.gabert.kyla.dataslot.WorkUnitProcessor;
import net.gabert.util.LogUtil;
import org.apache.log4j.Logger;

import java.lang.reflect.Method;

/**
 * 
 * @author Robert Gallas
 */
public class BusProxy implements Bus {
    private static final Logger LOGGER = LogUtil.getLogger();

    protected final DataSlotProvider dataSlotProvider;
    private final WorkUnitProcessor workUnitProcessor;
    private final Bus simpleBus;

    public BusProxy(KylaConfiguration config) {
        this.workUnitProcessor = new WorkUnitProcessor(config);
        this.dataSlotProvider = loadProvider(config.getDataSlotProviderClassName(), workUnitProcessor);
        this.simpleBus = new SimpleBus(dataSlotProvider);
        LOGGER.info(BusProxy.class.getSimpleName() + " created.");
    }

    private DataSlotProvider loadProvider(String dataSlotProviderConfig, WorkUnitProcessor workUnitProcessor) {
        try {
            Class<?> clazz = Class.forName(dataSlotProviderConfig);

            Method staticFactory = clazz.getMethod("getInstance", WorkUnitProcessor.class);
            DataSlotProvider dataSlotProvider = (DataSlotProvider)staticFactory.invoke(null, workUnitProcessor);

            return dataSlotProvider;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    public void start() {
        LOGGER.info(BusProxy.class.getSimpleName() + " starting.");
        workUnitProcessor.start();
        LOGGER.info(BusProxy.class.getSimpleName() + " started.");
    }

    public void shutdown() {
        workUnitProcessor.shutdown();
        dataSlotProvider.shutdown();
    }

    public void register(Endpoint endpoint) {
        dataSlotProvider.register(endpoint);
    }

    public void registerExclusive(Endpoint endpoint, String dataSlotId) {
        dataSlotProvider.registerExclusive(endpoint, dataSlotId);
    }

    public void register(Endpoint endpoint, String dataSlotId) {
        dataSlotProvider.register(endpoint, dataSlotId);
    }

    public void send(Message message) {
        Bus destinationBus = resolveBus(message);
        destinationBus.send(message);
    }

    /*
     * Method designed to resolve correct destination bus based on ACL
     */
    private Bus resolveBus(Message message) {
        return simpleBus;
    }

    public void await() {
        workUnitProcessor.await();
    }
}
