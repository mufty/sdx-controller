package net.gabert.sdx.heiko.core;

import net.gabert.sdx.heiko.configuration.ConfigurationLoader;
import net.gabert.sdx.heiko.configuration.schema.ConnectorConfig;
import net.gabert.sdx.heiko.configuration.schema.DriverConfig;
import net.gabert.sdx.heiko.configuration.schema.ServiceConfig;
import net.gabert.sdx.heiko.configuration.schema.HeikoConfiguration;
import net.gabert.sdx.heiko.mountpoint.MountService;
import net.gabert.sdx.heiko.mountpoint.MountServiceLocal;
import net.gabert.sdx.kyla.core.BusProxy;
import net.gabert.util.LogUtil;
import org.slf4j.Logger;

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
        initializeHeikoServices();
        mountDevices();
        mountConnectors();
        mountServices();
        createStaticMappings();
        startMountPoints();
    }

    private void initializeHeikoServices() {
        LOGGER.info("PHASE[1]: Initialize HEIKO services.");

        HEIKO_SERVICE_REGISTRY.put(MountService.class, new MountServiceLocal(this.busProxy));
        LOGGER.info("{} initialized.", HEIKO_SERVICE_REGISTRY.get(MountService.class).getClass().getSimpleName());

        HEIKO_SERVICE_REGISTRY.put(MappingService.class, new MappingService());
        LOGGER.info("{} initialized.", HEIKO_SERVICE_REGISTRY.get(MappingService.class).getClass().getSimpleName());
    }

    private void startBus() {
        LOGGER.info("PHASE[2]: Start KYLA.");
        this.busProxy = BusProxy.start(this.config.bus.configUrl);
    }

    private void mountDevices() {
        LOGGER.info("PHASE[3]: Mount devices.");

        MountService mountService = getService(MountService.class);
        for (DriverConfig driver : this.config.drivers) {
            mountService.mount(driver);
        }
    }

    private void mountConnectors() {
        LOGGER.info("PHASE[4]: Mount connectors.");

        MountService mountService = getService(MountService.class);
        for (ConnectorConfig connector : this.config.connectors) {
             mountService.mount(connector);
        }
    }

    private void mountServices() {
        LOGGER.info("PHASE[5]: Mount services.");

        MountService mountService = getService(MountService.class);
        for (ServiceConfig serviceCfg : this.config.services) {
            mountService.mount(serviceCfg);
        }
    }

    private void createStaticMappings() {
        LOGGER.info("PHASE[6]: Create static mappings.");
        for (Map.Entry<String, String> mapping : this.config.mappings.entrySet()) {
            Controller.getService(MappingService.class).link(mapping.getKey(), mapping.getValue());
        }
    }

    private void startMountPoints() {
        LOGGER.info("PHASE[7]: Start mount points.");
        getService(MountService.class).startMontPoints();
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
