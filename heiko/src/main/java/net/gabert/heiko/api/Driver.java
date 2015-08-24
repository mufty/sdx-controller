package net.gabert.heiko.api;

import net.gabert.kyla.api.Bus;

public abstract class Driver {
    private Bus bus;
    private String contextRoot;

    protected void publish(String path, Object value) {
        this.bus.send(null);
    }

    public abstract Object getValue(String path);

    public abstract void setValue(String path, Object value);

    public abstract void onListenerRegistered(String path);

    public abstract Object call(String path, Object ... params);
}
