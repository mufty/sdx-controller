package net.gabert.heiko.core;

public class Controller {
    private final HeikoJsonConfiguration config;

    private Controller(HeikoJsonConfiguration config) {
        this.config = config;
    }

    public static Controller boot(String configFileUrl) {
        HeikoJsonConfiguration config = new HeikoJsonConfiguration(configFileUrl);
        return new Controller(config);
    }

    public static Controller bootDefaultConfiguration() {
        HeikoJsonConfiguration config = new HeikoJsonConfiguration();
        return new Controller(config);
    }
}
