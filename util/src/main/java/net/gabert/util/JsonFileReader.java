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

    public static <T> T fromString(String jsonString, Class<T> mappedClass) {
        return fromJson(jsonString, mappedClass);
    }

    public static <T> T fromFile(String fileUrl, Class<T> mappedClass) {
        return fromJson(asJsonObject(fileUrl), mappedClass);
    }

    public static JsonObject asJsonObject(String jsonFileUrl) {
        try {
            String configContent = FileReader.readFile(jsonFileUrl);
            JsonObject json = new JsonParser().parse(configContent).getAsJsonObject();

            LOGGER.info("Loaded configuration file from: {}", jsonFileUrl);

            return json;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T fromJson(JsonObject json, Class<T> mappedClass) {
        return GSON.fromJson(json, mappedClass);
    }

    public static <T> T fromJson(String jsonString, Class<T> mappedClass) {
        return GSON.fromJson(jsonString, mappedClass);
    }
}
