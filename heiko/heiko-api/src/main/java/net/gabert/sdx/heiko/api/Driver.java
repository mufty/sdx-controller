package net.gabert.sdx.heiko.api;

import net.gabert.sdx.kyla.api.Bus;

import java.util.Map;

public abstract class Driver {
    private Bus bus;
    private Map<String, Object> initParams;

    protected void publish(String path, Object value) {
        this.bus.send(null);
    }

    public abstract Object getValue(String path);

    public abstract void setValue(String path, Object value);

    public abstract void onListenerRegistered(String path);

    public abstract Object call(String relativePath, Object ... params);
}
