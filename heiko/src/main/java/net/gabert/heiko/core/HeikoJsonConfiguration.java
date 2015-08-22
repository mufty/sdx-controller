package net.gabert.heiko.core;

import net.gabert.util.JsonConfigLoader;

import java.util.Map;

public class HeikoJsonConfiguration extends JsonConfigLoader {
    private static final String DEFAULT_CONFIG_CFG = "classpath:heikocfg.json";

    private static final String ALIASES_KEY = "aliases";
    private static final String MOUNTPOINTS_KEY = "mountPoints";

    private final Map<String, String> aliases;

    public HeikoJsonConfiguration() {
        this(DEFAULT_CONFIG_CFG);
    }

    public HeikoJsonConfiguration(String fileName) {
        super(fileName);
        this.aliases = getMap(ALIASES_KEY);
    }

    public void getMountPoints() {

    }
}
