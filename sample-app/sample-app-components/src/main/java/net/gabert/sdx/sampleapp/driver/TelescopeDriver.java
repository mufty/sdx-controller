package net.gabert.sdx.sampleapp.driver;

import net.gabert.sdx.heiko.api.Driver;

public class TelescopeDriver extends Driver {
    @Override
    public Object getValue(String path) {
        System.out.println(">>>>> GET " + path);
        return null;
    }

    @Override
    public void setValue(String path, Object value) {
        System.out.println(">>>>> SET " + path);

    }

    @Override
    public void onListenerRegistered(String path) {
        System.out.println(">>>>> OLR " + path);
    }

    @Override
    public Object call(String path, Object... params) {
        System.out.println(">>>>> CALL " + path);
        return null;
    }
}
