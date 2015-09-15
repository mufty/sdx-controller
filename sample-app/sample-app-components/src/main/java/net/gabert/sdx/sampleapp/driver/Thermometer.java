package net.gabert.sdx.sampleapp.driver;


import net.gabert.sdx.heiko.component.Driver;

import java.util.Map;

/**
 *
 * @author Robert Gallas
 */
public class Thermometer extends Driver {
    private final SimpleDB db = new SimpleDB("/temperature");

    @Override
    public void start(Map<String, Object> initParams) {
        db.setValue("/temperature", 20);
    }

    @Override
    public void stop() {

    }

    @Override
    public Object getValue(String path) {
        return db.getValue(path);
    }

    @Override
    public void setValue(String path, Object value) {
        db.setValue(path, value);
        publish(path, value);
    }

    @Override
    public void onListenerRegistered(String path) {

    }

    @Override
    public Object call(String relativePath, Object... params) {
        return null;
    }
}
