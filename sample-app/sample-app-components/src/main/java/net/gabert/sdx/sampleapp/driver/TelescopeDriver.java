package net.gabert.sdx.sampleapp.driver;

import net.gabert.sdx.heiko.api.Driver;

/**
 *
 * @author Robert Gallas
 */
public class TelescopeDriver extends Driver {
    private int azimuth;
    private int altitude;

    private final SimpleValueHolder db = new SimpleValueHolder("/azimuth", "/altitude");

    @Override
    public Object getValue(String path) {
        return db.getValue(path);

    }

    @Override
    public void setValue(String path, Object value) {
        db.setValue(path, value);
    }

    @Override
    public void onListenerRegistered(String path) {
    }

    @Override
    public Object call(String path, Object... params) {
        return null;
    }
}
