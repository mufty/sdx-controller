package net.gabert.heiko.configuration;

import net.gabert.util.JsonConfigLoader;

public class HeikoJsonConfigurationLoader extends JsonConfigLoader {
    private static final String DEFAULT_CONFIG_CFG = "classpath:heikocfg.json";

    public HeikoJsonConfigurationLoader() {
        this(DEFAULT_CONFIG_CFG);
    }

    public HeikoJsonConfigurationLoader(String fileName) {
        super(fileName);
    }

    public HeikoConfiguration getConfiguration() {
        return fromJson(HeikoConfiguration.class);
    }
}
