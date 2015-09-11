package net.gabert.sdx.heiko.ctx;

import net.gabert.sdx.heiko.api.Service;
import net.gabert.sdx.heiko.mountpoint.MountPoint;

/**
 * @author Robert Gallas
 */
final class DeviceContext extends Context {
    DeviceContext(MountPoint mountPoint) { super(mountPoint); }

    @Override
    public void setValue(String contextRelativePath, Object value) {

    }

    @Override
    public void setValue(String contextRelativePath, Object value, Service.Callback callback) {

    }

    @Override
    public void getValue(String contextRelativePath, Service.Callback callback) {

    }

    @Override
    public void call(String contextRelativePath, Object[] params, Service.Callback callback) {

    }
}
