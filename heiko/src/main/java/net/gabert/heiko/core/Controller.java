package net.gabert.heiko.core;

import net.gabert.heiko.configuration.HeikoConfiguration;
import net.gabert.heiko.configuration.HeikoJsonConfigurationLoader;
import net.gabert.heiko.configuration.MountPointConfig;
import net.gabert.heiko.mountpoint.MountService;
import net.gabert.heiko.mountpoint.MountServiceLocal;
import net.gabert.kyla.api.KylaConfiguration;
import net.gabert.kyla.bus.BusProxy;
import net.gabert.kyla.configuration.DefaultKylaConfiguration;
import net.gabert.kyla.configuration.JsonKylaConfiguration;
import net.gabert.util.Alias;
import net.gabert.util.FallbackMethodCaller;
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
        KylaConfiguration kylaCfg =
                FallbackMethodCaller.getInstance(KylaConfiguration.class,
                                                 new JsonKylaConfiguration(this.config.bus.configUrl),
                                                 new DefaultKylaConfiguration());
        this.bus = new BusProxy(kylaCfg);
        this.bus.start();
    }

    private void mountEndpoints() {
        LOGGER.info("Mounting endpoints.");

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

    public static Controller boot() {
        return boot(new HeikoJsonConfigurationLoader());
    }

    public static Controller boot(String configFileUrl) {
        return boot(new HeikoJsonConfigurationLoader(configFileUrl));
    }

    private static Controller boot(HeikoJsonConfigurationLoader loader) {
        LOGGER.info("Booting controller.");
        Controller controller = new Controller(loader.getConfiguration());
        controller.init();
        return controller;
    }
}
