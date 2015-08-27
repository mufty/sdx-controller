package net.gabert.sdx.sampleapp.application;

import net.gabert.sdx.heiko.api.Application;
import net.gabert.util.LogUtil;
import org.apache.log4j.Logger;

import java.util.Map;

public class SampleApplication extends Application {
    private static final Logger LOGGER = LogUtil.getLogger();

    @Override
    public void init(Map<String, Object> initParams) {
        LOGGER.info("Application started");
    }
}
