package net.gabert.sdx.heiko.mountpoint;

import net.gabert.sdx.heiko.configuration.schema.DriverConfig;
import net.gabert.sdx.heiko.configuration.schema.ServiceConfig;

public interface MountService {

    void mount(DriverConfig driverConfig);

    void mount(ServiceConfig serviceConfig);

    MountPoint getMountPoint(String requestPath);
}
