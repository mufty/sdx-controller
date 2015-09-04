package net.gabert.sdx.heiko.mountpoint;

import net.gabert.sdx.heiko.configuration.schema.DriverConfig;
import net.gabert.sdx.heiko.configuration.schema.ServiceConfig;

/**
 *
 * @author Robert Gallas
 */
public interface MountService {

    void mount(DriverConfig driverConfig);

    void mount(ServiceConfig serviceConfig);

    void startMontPoints();
}
