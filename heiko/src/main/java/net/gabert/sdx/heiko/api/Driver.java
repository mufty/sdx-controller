package net.gabert.sdx.heiko.api;

import net.gabert.sdx.heiko.mountpoint.DriverMountPoint;
import net.gabert.sdx.kyla.api.Bus;

import java.util.Map;

/**
 *
 * @author Robert Gallas
 */
public abstract class Driver {
    private DriverMountPoint mountPoint;
    private Map<String, Object> initParams;

    protected void publish(String path, Object value) {
        mountPoint.send(null);
    }

    public abstract Object getValue(String path);

    public abstract void setValue(String path, Object value);

    public abstract void onListenerRegistered(String path);

    public abstract Object call(String path, Object ... params);
}
