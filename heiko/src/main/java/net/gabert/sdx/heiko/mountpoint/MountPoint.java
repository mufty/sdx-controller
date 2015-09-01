package net.gabert.sdx.heiko.mountpoint;

import net.gabert.sdx.heiko.core.HeikoMessage;
import net.gabert.sdx.kyla.api.Endpoint;
import net.gabert.sdx.kyla.core.BusProxy;

import java.util.Map;

public abstract class MountPoint extends Endpoint<HeikoMessage> {
    private final String mountPointContextRoot;
    private final Map<String, Object> initParams;
    private final BusProxy busProxy;

    public MountPoint(String mountPointContextRoot,
                      Map<String, Object> initParams,
                      BusProxy busProxy) {
        super(busProxy);
        this.mountPointContextRoot = mountPointContextRoot;
        this.initParams = initParams;
        this.busProxy = busProxy;
    }

    public void init() {
        busProxy.registerExclusive(this, mountPointContextRoot);
    }

    public String getMountPointContextRoot() {
        return mountPointContextRoot;
    }

    protected Map<String, Object> getInitParams() {
        return initParams;
    }

    @Override
    public String toString() {
        return "[" +
                mountPointContextRoot +
                " -> " +
                getImplementationClass() +
                "]";
    }

    protected abstract Class getImplementationClass();
}
