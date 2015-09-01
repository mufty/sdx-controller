package net.gabert.sdx.heiko.core;

import net.gabert.sdx.heiko.api.Service;
import net.gabert.sdx.heiko.configuration.ConfigurationLoader;
import net.gabert.sdx.heiko.configuration.schema.ServiceConfig;
import net.gabert.sdx.heiko.configuration.schema.HeikoConfiguration;
import net.gabert.sdx.heiko.mountpoint.MountService;
import net.gabert.sdx.heiko.mountpoint.MountServiceLocal;
import net.gabert.sdx.kyla.core.BusProxy;
import net.gabert.util.LogUtil;
import net.gabert.util.ObjectUtil;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {
    private static final Logger LOGGER = LogUtil.getLogger();

    private final HeikoConfiguration config;
    private BusProxy busProxy;

    private final Map<Class<?>, Object> heikoServiceRegistry = new HashMap<>();
    private final List<Service> serviceRegistry = new ArrayList<>();

    private Controller(HeikoConfiguration config) {
        this.config = config;
    }

    // ----- INITIALIZATION -----
    private void init() {
        startBus();
        initializeServices();
        mountEndpoints();
        startApplications();
    }

    private void initializeServices() {
        LOGGER.info("--- PHASE --- Initializing services.");
        heikoServiceRegistry.put(MountService.class, new MountServiceLocal(this.busProxy));
    }

    private void startBus() {
        LOGGER.info("--- PHASE --- Starting busProxy.");
        this.busProxy = BusProxy.start(this.config.bus.configUrl);
    }

    private void mountEndpoints() {
        LOGGER.info("--- PHASE --- Processing mountpoints.");

        MountService mountService = getService(MountService.class);
        mountService.mount(this.config.mountPoints);
    }

    private void startApplications() {
        LOGGER.info("--- PHASE --- Starting services.");

        for (ServiceConfig appCfg : this.config.services) {
            Service appInstance = ObjectUtil.newInstance(appCfg.className);
            ObjectUtil.injectByValue(appInstance, busProxy);
            serviceRegistry.add(appInstance);
            appInstance.init(appCfg.initParams);
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

    // ----- UTILITY -----
    private <T> T getService(Class<? extends T> serviceClass) {
        return (T) heikoServiceRegistry.get(serviceClass);
    }
}
