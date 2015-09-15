package net.gabert.sdx.heiko.component;

import net.gabert.sdx.heiko.ctx.Context;
import net.gabert.sdx.heiko.mountpoint.ServiceMountPoint;

import java.util.Map;

/**
 *
 * @author Robert Gallas
 */
public abstract class Service implements Component {
    private ServiceMountPoint mountPoint;

    protected Context getPathContext(String contextRoot) {
        return Context.getPathContext(contextRoot, mountPoint);
    }

    protected Context getPathContext() {
        return getPathContext("");
    }

    protected Context getDeviceContext(String deviceMountPoint) {
        return Context.getPathContext(deviceMountPoint, mountPoint);
    }

    public static interface Callback {
        void done(Object reponse);
    }
}
