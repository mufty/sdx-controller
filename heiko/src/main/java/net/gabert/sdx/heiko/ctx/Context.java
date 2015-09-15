package net.gabert.sdx.heiko.ctx;

import net.gabert.sdx.heiko.component.Callback;
import net.gabert.sdx.heiko.component.Service;
import net.gabert.sdx.heiko.core.HeikoMessage;
import net.gabert.sdx.heiko.mountpoint.ComponentMountPoint;
import net.gabert.sdx.kyla.api.Endpoint;

/**
 * @author Robert Gallas
 */
public abstract class Context {
    protected final ComponentMountPoint mountPoint;

    protected Context(ComponentMountPoint mountPoint) { this.mountPoint = mountPoint; }

    public static Context getPathContext(String contextRoot, ComponentMountPoint sourceMountPoint) {
        return new PathContext(contextRoot, sourceMountPoint);
    }

    public static Context getDeviceContext(String contextRoot, ComponentMountPoint sourceMountPoint) {
        return new DeviceContext(contextRoot, sourceMountPoint);
    }

    protected Endpoint.Message<HeikoMessage> toKylaMessage(String dataSlotId, HeikoMessage heikoMessage) {
        return mountPoint.createMessage(dataSlotId, heikoMessage);
    }

    public abstract void setValue(String path, Object value);

    public abstract void setValue(String path, Object value, Callback callback);

    public abstract void getValue(String path, Callback callback);

    public abstract void call(String path, Object[] params, Callback callback);
}
