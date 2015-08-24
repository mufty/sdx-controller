package net.gabert.sdx.heiko.configuration;

import net.gabert.sdx.heiko.configuration.schema.HeikoConfiguration;
import net.gabert.util.JsonTransformation;
import net.gabert.util.Transformation;

import java.util.HashMap;
import java.util.Map;

public class ConfigurationLoader {
    private static Map<String, Transformation<HeikoConfiguration>> configLoaders = new HashMap<>();

    static {
        configLoaders.put("json", new JsonTransformation<HeikoConfiguration>());
    }


    public static HeikoConfiguration load(String configFileUrl) {
        return configLoaders.get(configFileSuffix(configFileUrl)).fromFile(configFileUrl);
    }

    private static String configFileSuffix(String configFileUrl) {
        return configFileUrl.substring(configFileUrl.lastIndexOf("."), configFileUrl.length());
    }
}
