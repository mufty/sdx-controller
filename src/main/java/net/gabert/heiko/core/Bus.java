package net.gabert.heiko.core;

import net.gabert.kyla.api.Endpoint;

/**
 * Created by Family on 11. 8. 2015.
 */
public class Bus implements MountService {
    public void set(String path, Object val1) {

    }

    public Object read(String path) {
        return null;
    }

    public void registerListener(String path, Object listener) {

    }

    @Override
    public void mount(String mountPoint, AbstractDriver driver) {
        driver.setMountPoint(mountPoint);
    }

    @Override
    public void mount(String mountPoint, Endpoint edpoint) {

    }
}
