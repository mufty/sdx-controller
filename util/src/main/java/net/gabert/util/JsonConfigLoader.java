package net.gabert.util;

import com.google.gson.*;

import java.util.HashMap;
import java.util.Map;

public abstract class JsonConfigLoader {
    private final JsonObject json;

    protected JsonConfigLoader(String configFileName) {
        try {
            String configContent = FileReader.readFile(configFileName);
            this.json = new JsonParser().parse(configContent).getAsJsonObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected int getAsInt(String nodePath) {
        return json.get(nodePath).getAsInt();
    }

    protected String getAsString(String nodePath) {
        return json.get(nodePath).getAsString();
    }

    protected <T> Map<String, T> getMap(final String nodePath) {
        if (json.get(nodePath) == null) {
            return new HashMap<>();
        }

        final JsonObject values = json.get(nodePath).getAsJsonObject();

        return new HashMap<String, T>() {{
            for (Entry<String, JsonElement> value : values.entrySet() ) {
                put(value.getKey(), (T)value.getValue());
            }
        }};
    }

    protected <T> T fromJson(Class<T> configClass) {
        Gson gson = new Gson();
        return gson.fromJson(json, configClass);
    }
}
