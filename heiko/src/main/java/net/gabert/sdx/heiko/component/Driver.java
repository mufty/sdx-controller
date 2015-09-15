package net.gabert.sdx.heiko.component;

import net.gabert.sdx.heiko.mountpoint.DriverMountPoint;
import net.gabert.sdx.kyla.api.Bus;

import java.util.Map;

/**
 *
 * @author Robert Gallas
 */
public abstract class Driver implements Component {
    private DriverMountPoint mountPoint;

    protected void publish(String path, Object value) {
        mountPoint.send(null);
    }

    public abstract Object getValue(String path);

    public abstract void setValue(String path, Object value);

    public abstract void onListenerRegistered(String path);

    public abstract Object call(String path, Object[] params);
}
