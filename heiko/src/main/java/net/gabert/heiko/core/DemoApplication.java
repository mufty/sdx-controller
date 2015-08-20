package net.gabert.heiko.core;

import net.gabert.kyla.api.Endpoint;
import net.gabert.kyla.bus.BusProxy;

/**
 * Created by Family on 20. 8. 2015.
 */
public class DemoApplication extends Application {
    protected DemoApplication(BusProxy busProxy) {
        super(busProxy);
    }

    public void run() {
        Context context = getContext("/a/b/c");
        context.setValue("/x/y/z", 1);
        int val = (int)context.getValue("/x/y/z");
    }
}
