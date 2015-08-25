package net.gabert.sdx.heiko.configuration;

import net.gabert.sdx.heiko.configuration.schema.HeikoConfiguration;
import net.gabert.util.Alias;
import net.gabert.util.FileReader;
import net.gabert.util.JsonTransformation;
import net.gabert.util.Transformation;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class ConfigurationLoader {
    private static Map<String, Transformation<HeikoConfiguration>> configLoaders = new HashMap<>();

    static {
        configLoaders.put("json", new JsonTransformation<HeikoConfiguration>());
    }


    public static HeikoConfiguration load(String configFileUrl) {
        try {
            String fileContentRaw = FileReader.readFile(configFileUrl);
            Transformation<HeikoConfiguration> transformation = configLoaders.get(configFileSuffix(configFileUrl));
            HeikoConfiguration cfg1stPass = transformation.fromFile(configFileUrl, HeikoConfiguration.class);

            String fileContentNormalized = Alias.normalize(fileContentRaw, cfg1stPass.aliases);
            return transformation.fromString(fileContentNormalized, HeikoConfiguration.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static String configFileSuffix(String configFileUrl) {
        return configFileUrl.substring(configFileUrl.lastIndexOf(".") + 1, configFileUrl.length());
    }
}
