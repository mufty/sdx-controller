package net.gabert.heiko.service;

import net.gabert.heiko.driver.AbstractDriver;
import net.gabert.heiko.util.PathMap;
import net.gabert.kyla.api.Endpoint;

public class MountServiceLocal implements MountService {
    private static final String PATH_SEPARATOR = "/";

    private PathMap<String> pathMap = new PathMap<>(PATH_SEPARATOR);

    @Override
    public void register(String mountPoint, AbstractDriver driver) {
        driver.setMountPoint(this, mountPoint);
    }

    @Override
    public void mount(String mountPoint, Endpoint edpoint) {

    }

    @Override
    public void mount(String mountPoint, String dataslotId) {
        pathMap.put(mountPoint, dataslotId);
    }

    @Override
    public String getMount(String path) {
        return pathMap.get(path);
    }

    @Override
    public String getPathSeparator() {
        return PATH_SEPARATOR;
    }
}
