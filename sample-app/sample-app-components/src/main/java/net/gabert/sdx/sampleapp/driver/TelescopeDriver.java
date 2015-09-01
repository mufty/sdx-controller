package net.gabert.sdx.sampleapp.driver;

import net.gabert.sdx.heiko.api.Driver;

public class TelescopeDriver extends Driver {
    private int azimuth;
    private int altitude;

    @Override
    public Object getValue(String path) {
        if (path.equals("/azimuth")) {
            return this.azimuth;
        } else if (path.equals("/altitude")) {
            return this.altitude;
        }
        return null;
    }

    @Override
    public void setValue(String path, Object value) {
        if (path.equals("/azimuth")) {
            this.azimuth = (int)value;
        } else if (path.equals("/altitude")) {
            this.altitude = (int)value;
        }

    }

    @Override
    public void onListenerRegistered(String path) {
    }

    @Override
    public Object call(String path, Object... params) {
        return null;
    }
}
