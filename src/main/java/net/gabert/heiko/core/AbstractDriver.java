package net.gabert.heiko.core;

import net.gabert.kyla.api.Endpoint;

/**
 * Created by Family on 11. 8. 2015.
 */
public abstract class AbstractDriver implements Driver{
    private String mountPoint;
    private Bus bus;

    public String getMountPoint() {
        return mountPoint;
    }

    public void setMountPoint(String mountPoint) {
        this.mountPoint = mountPoint;
    }

    protected final void mount(String subPath, Endpoint endpoint) {

    }
}
