package net.gabert.sdx.heiko.mountpoint;

import net.gabert.sdx.kyla.api.Endpoint;
import net.gabert.sdx.kyla.core.BusProxy;

import java.util.Map;

public abstract class MountPoint {
    private final String mountPointContextRoot;
    private final Map<String, Object> initParams;
    private final BusProxy busProxy;
    private final HeikoRpcEndpoint endpoint;

    public MountPoint(String mountPointContextRoot,
                      Map<String, Object> initParams,
                      BusProxy busProxy) {
        this.mountPointContextRoot = mountPointContextRoot;
        this.initParams = initParams;
        this.busProxy = busProxy;
        this.endpoint = new HeikoRpcEndpoint(this.busProxy);
    }

    public void init() {
        busProxy.registerExclusive(endpoint, mountPointContextRoot);
    }

    public String getMountPointContextRoot() {
        return mountPointContextRoot;
    }

    @Override
    public String toString() {
        return "["+mountPointContextRoot+" -> " + getImplementationClass() + "]";
    }

    protected abstract Class getImplementationClass();
}
