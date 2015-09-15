package net.gabert.sdx.heiko.component;

import net.gabert.sdx.heiko.ctx.Context;
import net.gabert.sdx.heiko.mountpoint.ConnectorMountPoint;

/**
 * @author Robert Gallas
 */
public abstract class Connector implements Component {
    private ConnectorMountPoint mountPoint;

    protected Context getPathContext(String contextRoot) {
        return Context.getPathContext(contextRoot, mountPoint);
    }

    protected Context getPathContext() {
        return getPathContext("");
    }

    protected Context getDeviceContext(String deviceMountPoint) {
        return Context.getPathContext(deviceMountPoint, mountPoint);
    }


}
