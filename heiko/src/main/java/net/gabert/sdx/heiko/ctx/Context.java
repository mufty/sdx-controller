package net.gabert.sdx.heiko.ctx;

import net.gabert.sdx.heiko.api.Service;
import net.gabert.sdx.heiko.core.HeikoMessage;
import net.gabert.sdx.heiko.mountpoint.MountPoint;
import net.gabert.sdx.kyla.api.Endpoint;

/**
 * @author Robert Gallas
 */
public abstract class Context {
    protected final MountPoint mountPoint;

    protected Context(MountPoint mountPoint) { this.mountPoint = mountPoint; }

    public static Context getPathContext(String contextRoot, MountPoint sourceMountPoint) {
        return new PathContext(contextRoot, sourceMountPoint);
    }

    public static Context getDeviceContext(String contextRoot, MountPoint sourceMountPoint) {
        return new DeviceContext(contextRoot, sourceMountPoint);
    }

    protected Endpoint.Message<HeikoMessage> toKylaMessage(String dataSlotId, HeikoMessage heikoMessage) {
        return mountPoint.createMessage(dataSlotId, heikoMessage);
    }

    public abstract void setValue(String path, Object value);

    public abstract void setValue(String path, Object value, Service.Callback callback);

    public abstract void getValue(String path, Service.Callback callback);

    public abstract void call(String path, Object[] params, Service.Callback callback);
}
