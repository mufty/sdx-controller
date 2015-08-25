package net.gabert.sdx.heiko.mountpoint;

import net.gabert.sdx.heiko.configuration.schema.MountPointConfig;

import java.util.List;

public interface MountService {

    void mount(MountPointConfig mountPointConfig) throws NoSuchMethodException, ClassNotFoundException;

    void mount(List<MountPointConfig> mountPointConfigs);

    MountPoint getMountPoint(String requestPath);
}
