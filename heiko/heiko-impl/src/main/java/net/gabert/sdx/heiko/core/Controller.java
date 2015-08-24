package net.gabert.sdx.heiko.core;

import net.gabert.sdx.heiko.configuration.ConfigurationLoader;
import net.gabert.sdx.heiko.configuration.schema.HeikoConfiguration;
import net.gabert.sdx.heiko.configuration.schema.MountPointConfig;
import net.gabert.sdx.heiko.mountpoint.MountService;
import net.gabert.sdx.heiko.mountpoint.MountServiceLocal;
import net.gabert.kyla.configuration.KylaConfiguration;
import net.gabert.kyla.bus.BusProxy;
import net.gabert.util.Alias;
import net.gabert.util.JsonTransformation;
import net.gabert.util.LogUtil;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller {
    private static final Logger LOGGER = LogUtil.getLogger();

    private final HeikoConfiguration config;
    private BusProxy bus;

    private final Map<Class<?>, Object> serviceRegistry = new HashMap<>();

    private Controller(HeikoConfiguration config) {
        this.config = config;
    }

    private void init() {
        startBus();
        initializeServices();
        mountEndpoints();
        startApplications();
    }

    private void initializeServices() {
        LOGGER.info("Initializing services.");
        serviceRegistry.put(MountService.class, new MountServiceLocal(this.bus));
    }

    private void startBus() {
        LOGGER.info("Starting bus.");
        KylaConfiguration kylaCfg = new JsonTransformation<KylaConfiguration>().fromFile(this.config.bus.configUrl);
        this.bus = new BusProxy(kylaCfg);
        this.bus.start();
    }

    private void mountEndpoints() {
        LOGGER.info("Processing mountpoints.");

        MountService mountService = getService(MountService.class);
        mountService.mount(normalize(this.config.mountPoints));
    }

    private List<MountPointConfig> normalize(List<MountPointConfig> mountPoints) {
        for (MountPointConfig mntConfig : mountPoints) {
            mntConfig.className = Alias.normalize(mntConfig.className, this.config.aliases);
        }

        return mountPoints;
    }

    private void startApplications() {
        LOGGER.info("Starting applications.");
    }

    private <T> T getService(Class<? extends T> serviceClass) {
        return (T) serviceRegistry.get(serviceClass);
    }

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
}
