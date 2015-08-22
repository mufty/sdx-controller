package net.gabert.heiko.core;

import net.gabert.heiko.configuration.HeikoConfiguration;
import net.gabert.heiko.configuration.HeikoJsonConfigurationLoader;
import net.gabert.kyla.api.KylaConfiguration;
import net.gabert.kyla.bus.BusProxy;
import net.gabert.kyla.configuration.DefaultKylaConfiguration;
import net.gabert.kyla.configuration.JsonKylaConfiguration;
import net.gabert.util.FallbackMethodCaller;
import net.gabert.util.LogUtil;
import org.apache.log4j.Logger;

public class Controller {
    private static final Logger LOGGER = LogUtil.getLogger();

    private final HeikoConfiguration config;
    private BusProxy bus;

    private Controller(HeikoConfiguration config) {
        this.config = config;
    }

    private void init() {
        startBus();
        mountEndpoints();
        startApplications();
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
    }

    private void startApplications() {
        LOGGER.info("Starting applications.");
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
