package net.gabert.sdx.sampleapp.service;

import net.gabert.sdx.heiko.api.Service;
import net.gabert.sdx.heiko.ctx.Context;
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
        Context telescope = getPathContext("");

        telescope.setValue("/iot/telescopes/palo-alto/azimuth", 12);
        telescope.setValue("/iot/telescopes/palo-alto/azimuth", 25);

//        telescope.getValue("/azimuth", new Callback() {
//            @Override
//            public void done(Object reponse) {
//                System.out.println(reponse);
//            }
//        });
//
//        telescope.getValue("/altitude", new Callback() {
//            @Override
//            public void done(Object reponse) {
//                System.out.println(reponse);
//            }
//        });

//        performanceTesting(telescope);
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
        for (int i=0; i<10_000; i++) {
            telescope.setValue("/azimuth", 12);
        }
    }

    @Override
    public void close() {

    }
}
