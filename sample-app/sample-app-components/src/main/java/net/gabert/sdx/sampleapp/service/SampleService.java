package net.gabert.sdx.sampleapp.service;

import net.gabert.sdx.heiko.api.Service;
import net.gabert.util.LogUtil;
import org.slf4j.Logger;

import java.util.Arrays;
import java.util.Map;

import static net.gabert.util.LogUtil.sout;

/**
 *
 * @author Robert Gallas
 */
public class SampleService extends Service {
    @Override
    public void init(Map<String, Object> initParams) {
        Context telescope = getContext("/iot/telescopes/palo-alto");
        telescope.setValue("/azimuth", 12);
        telescope.setValue("/altitude", 25);

        sout(telescope.getValue("/azimuth"));
        sout(telescope.getValue("/altitude"));
        sout(telescope.call("/snapshot", params(100, 5, 3200)));
    }

    @Override
    public void close() {

    }
}
