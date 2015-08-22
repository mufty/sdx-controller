package net.gabert.heiko.core;

import net.gabert.heiko.configuration.HeikoConfiguration;
import net.gabert.heiko.configuration.HeikoJsonConfigurationLoader;

public class Controller {
    private final HeikoConfiguration config;

    private Controller(HeikoConfiguration config) {
        this.config = config;
    }

    public static Controller boot(String configFileUrl) {
        HeikoJsonConfigurationLoader loader = new HeikoJsonConfigurationLoader(configFileUrl);
        return new Controller(loader.getConfiguration());
    }

    public static Controller bootDefaultConfiguration() {
        HeikoJsonConfigurationLoader loader = new HeikoJsonConfigurationLoader();
        return new Controller(loader.getConfiguration());
    }
}
