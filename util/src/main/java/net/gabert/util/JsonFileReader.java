package net.gabert.util;

import com.google.gson.*;
import org.slf4j.Logger;

/**
 *
 * @author Robert Gallas
 */
public class JsonFileReader {
    private static final Logger LOGGER = LogUtil.getLogger();

    private static final Gson GSON = new Gson();

    public static <T> T fromString(String jsonString, Class<T> configClass) {
        return fromJson(jsonString, configClass);
    }

    public static <T> T fromFile(String fileUrl, Class<T> configClass) {
        return fromJson(asJsonObject(fileUrl), configClass);
    }

    private static JsonObject asJsonObject(String configFileName) {
        try {
            String configContent = FileReader.readFile(configFileName);
            JsonObject json = new JsonParser().parse(configContent).getAsJsonObject();

            LOGGER.info("Loaded configuration file from: {}", configFileName);

            return json;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T fromJson(JsonObject json, Class<T> configClass) {
        return GSON.fromJson(json, configClass);
    }

    private static <T> T fromJson(String jsonString, Class<T> configClass) {
        return GSON.fromJson(jsonString, configClass);
    }
}
