package net.gabert.kyla.configuration;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.gabert.kyla.api.KylaConfiguration;
import net.gabert.util.FileReader;
import net.gabert.util.JsonConfigLoader;

public class JsonKylaConfiguration extends JsonConfigLoader implements KylaConfiguration {
    private static final String DEFAULT_KYLA_CFG = "classpath:kylacfg.json";

    public JsonKylaConfiguration() {
        this(DEFAULT_KYLA_CFG);
    }

    public JsonKylaConfiguration(String fileName) {
        super(fileName);
    }

    @Override
    public int getWorkersCount() {
        return getAsInt("workersCount");
    }

    @Override
    public int getWorkQueueHardLimit() {
        return getAsInt("workerQueueHardLimit");
    }

    @Override
    public String getDataSlotProviderClassName() {
        return getAsString("dataSlotProviderClassName");
    }
}
