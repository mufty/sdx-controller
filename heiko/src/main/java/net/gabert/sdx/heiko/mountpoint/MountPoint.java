package net.gabert.sdx.heiko.mountpoint;

import net.gabert.sdx.kyla.core.BusProxy;

import java.util.Map;

/**
 *
 * @author Robert Gallas
 */
public abstract class MountPoint extends HeikoEndpoint {
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
        busProxy.register(this);
        busProxy.registerExclusive(this, mountPointContextRoot);
    }

    public String getMountPointContextRoot() {
        return mountPointContextRoot;
    }

    protected String getContextRelativePath(String absolutePath) {
        return absolutePath.replace(getMountPointContextRoot(), "");
    }

    protected Map<String, Object> getInitParams() {
        return initParams;
    }

    @Override
    public String toString() {
        return "[" +
                mountPointContextRoot +
                " -> " +
                "HC:FOOO" +
                "]";
    }
}
