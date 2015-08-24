package net.gabert.heiko.mountpoint;

import net.gabert.heiko.configuration.schema.MountPointConfig;

import java.util.List;

public interface MountService {

    void mount(String mountPointContextRoot, MountPoint mountPoint);

    void mount(MountPointConfig mountPointConfig) throws NoSuchMethodException, ClassNotFoundException;

    void mount(List<MountPointConfig> mountPointConfigs);

    MountPoint getMountPoint(String requestPath);

    String getPathSeparator();
}
