package net.gabert.sdx.heiko.mountpoint;

import net.gabert.sdx.kyla.core.BusProxy;

import java.util.Map;

/**
 *
 * @author Robert Gallas
 */
public abstract class MountPoint extends HeikoEndpoint {
    private final Map<String, Object> initParams;
    private final BusProxy busProxy;

    public MountPoint(Map<String, Object> initParams,
                      BusProxy busProxy) {
        super(busProxy);
        this.initParams = initParams;
        this.busProxy = busProxy;
    }

    public void init() {
        busProxy.register(this);
    }

    public abstract void start();

    protected Map<String, Object> getInitParams() {
        return initParams;
    }
}
