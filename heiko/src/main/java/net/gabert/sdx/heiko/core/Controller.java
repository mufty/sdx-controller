package net.gabert.sdx.heiko.core;

import net.gabert.sdx.heiko.configuration.ConfigurationLoader;
import net.gabert.sdx.heiko.configuration.schema.DriverConfig;
import net.gabert.sdx.heiko.configuration.schema.ServiceConfig;
import net.gabert.sdx.heiko.configuration.schema.HeikoConfiguration;
import net.gabert.sdx.heiko.mountpoint.MountService;
import net.gabert.sdx.heiko.mountpoint.MountServiceLocal;
import net.gabert.sdx.kyla.core.BusProxy;
import net.gabert.util.LogUtil;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Robert Gallas
 */
public class Controller {
    private static final Logger LOGGER = LogUtil.getLogger();

    private final HeikoConfiguration config;
    private BusProxy busProxy;

    private static final Map<Class<?>, Object> HEIKO_SERVICE_REGISTRY = new HashMap<>();

    private Controller(HeikoConfiguration config) {
        this.config = config;
    }

    // ----- INITIALIZATION -----
    private void init() {
        startBus();
        initializeServices();
        mountEndpoints();
        startServices();
    }

    private void initializeServices() {
        LOGGER.info("--- PHASE --- Initializing heiko services.");
        HEIKO_SERVICE_REGISTRY.put(MountService.class, new MountServiceLocal(this.busProxy));
    }

    private void startBus() {
        LOGGER.info("--- PHASE --- Starting busProxy.");
        this.busProxy = BusProxy.start(this.config.bus.configUrl);
    }

    private void mountEndpoints() {
        LOGGER.info("--- PHASE --- Processing mountpoints.");

        MountService mountService = getService(MountService.class);
        for (DriverConfig driver : this.config.drivers) {
            mountService.mount(driver);
        }
    }

    private void startServices() {
        LOGGER.info("--- PHASE --- Starting services.");

        MountService mountService = getService(MountService.class);
        for (ServiceConfig serviceCfg : this.config.services) {
            mountService.mount(serviceCfg);
        }
    }

    // ----- BOOTSTRAP -----
    private static final String DEFAULT_HEIKO_CONFIG_FILE = "classpath:heikocfg.json";
    public static Controller boot() {
        return boot(DEFAULT_HEIKO_CONFIG_FILE);
    }

    public static Controller boot(String configFileUrl) {
        return boot(ConfigurationLoader.load(configFileUrl));
    }

    private static Controller boot(HeikoConfiguration heikoConfiguration) {
        LOGGER.info("Booting controller.");
        Controller controller = new Controller(heikoConfiguration);
        controller.init();
        return controller;
    }

    // ----- SHUTDOWN -----
    public void shutDown() {
        this.busProxy.shutdown();
    }

    // ----- UTILITY -----
    public static <T> T getService(Class<? extends T> serviceClass) {
        return (T) HEIKO_SERVICE_REGISTRY.get(serviceClass);
    }
}
