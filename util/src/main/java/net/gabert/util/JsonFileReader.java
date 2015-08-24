package net.gabert.util;

import com.google.gson.*;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

public class JsonFileReader<T> {
    private static final Logger LOGGER = LogUtil.getLogger();

    private final String configFileUrl;

    public JsonFileReader(String configFileUrl) {
        this.configFileUrl = configFileUrl;
    }

    public T parse(Class<T> configClass) {
        return fromJson(asJsonObject(configFileUrl), configClass);
    }

    private JsonObject asJsonObject(String configFileName) {
        try {
            String configContent = FileReader.readFile(configFileName);
            JsonObject json = new JsonParser().parse(configContent).getAsJsonObject();

            LOGGER.info("Loaded configuration file from: " + configFileName);

            return json;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private T fromJson(JsonObject json, Class<T> configClass) {
        Gson gson = new Gson();
        return gson.fromJson(json, configClass);
    }
}
