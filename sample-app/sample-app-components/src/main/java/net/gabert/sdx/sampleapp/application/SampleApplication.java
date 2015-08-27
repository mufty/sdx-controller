package net.gabert.sdx.sampleapp.application;

import net.gabert.sdx.heiko.api.Application;
import net.gabert.util.LogUtil;
import org.apache.log4j.Logger;

import java.util.Map;

public class SampleApplication extends Application {
    private static final Logger LOGGER = LogUtil.getLogger();

    @Override
    public void init(Map<String, Object> initParams) {
        Context telescope = getContext("/iot/telescope");
        telescope.setValue("/azimuth", 12);
        telescope.setValue("/altitude", 25);
    }

    @Override
    public void close() {

    }
}
