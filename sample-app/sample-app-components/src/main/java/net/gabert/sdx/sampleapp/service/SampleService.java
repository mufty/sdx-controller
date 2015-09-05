package net.gabert.sdx.sampleapp.service;

import net.gabert.sdx.heiko.api.Service;
import net.gabert.util.LogUtil;
import net.gabert.util.TimeStat;
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

        performanceTesting(telescope);
    }

    private static void performanceTesting(Context telescope) {
        sout("Warming up ...");
        cycleCall(telescope);
        sout("Warmed");

        TimeStat ts = new TimeStat();
        ts.mark("start");
        cycleCall(telescope);
        ts.mark("stop");
        ts.printTimesBetweenMarkers();
    }

    private static void cycleCall(Context telescope) {
        Callback cb = new Callback() {@Override public void done(Object reponse) {}};
        for (int i=0; i<1_000_000; i++) {
            telescope.call("/snapshot", params(100, 5, 3200));
        }
    }

    @Override
    public void close() {

    }
}
