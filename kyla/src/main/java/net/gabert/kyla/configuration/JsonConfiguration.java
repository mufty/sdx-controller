package net.gabert.kyla.configuration;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.gabert.kyla.api.Configuration;
import net.gabert.util.FileReader;

public class JsonConfiguration implements Configuration {
    private static final String DEFAULT_KYLA_CFG = "classpath:kylacfg.json";
    private final JsonObject json;

    public JsonConfiguration () {
        this(DEFAULT_KYLA_CFG);
    }

    public JsonConfiguration (String fileName) {
        try {
            String configContent = FileReader.readFile(fileName);
            this.json = new JsonParser().parse(configContent).getAsJsonObject();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int getWorkersCount() {
        return json.get("workersCount").getAsInt();
    }

    @Override
    public int getWorkQueueHardLimit() {
        return json.get("workerQueueHardLimit").getAsInt();
    }

    @Override
    public String getDataSlotProviderClassName() {
        return json.get("dataSlotProviderClassName").getAsString();
    }
}
