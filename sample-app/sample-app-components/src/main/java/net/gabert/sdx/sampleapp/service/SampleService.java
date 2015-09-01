package net.gabert.sdx.sampleapp.service;

import net.gabert.sdx.heiko.api.Service;
import net.gabert.util.LogUtil;
import org.apache.log4j.Logger;

import java.util.Map;

public class SampleService extends Service {
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
