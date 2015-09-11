package net.gabert.sdx.heiko.api;

import net.gabert.sdx.heiko.ctx.Context;
import net.gabert.sdx.heiko.mountpoint.ServiceMountPoint;
import net.gabert.util.LogUtil;
import org.slf4j.Logger;

import java.util.Map;

/**
 *
 * @author Robert Gallas
 */
public abstract class Service {
    private ServiceMountPoint mountPoint;

    public abstract void init(Map<String, Object> initParams);

    public abstract void close();

    protected Context getPathContext(String contextRoot) {
        return Context.getPathContext(contextRoot, mountPoint);
    }

    protected Context getDeviceContext(String deviceMountPoint) {
        return Context.getPathContext(deviceMountPoint, mountPoint);
    }

    public static interface Callback {
        void done(Object reponse);
    }
}
