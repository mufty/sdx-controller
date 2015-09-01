package net.gabert.sdx.heiko.mountpoint;

import net.gabert.util.ObjectUtil;
import net.gabert.sdx.kyla.core.BusProxy;
import net.gabert.sdx.heiko.api.Driver;
import net.gabert.sdx.kyla.api.Endpoint;
import net.gabert.sdx.heiko.configuration.schema.DriverConfig;
import net.gabert.sdx.heiko.core.HeikoMessage;
import net.gabert.util.LogUtil;
import org.apache.log4j.Logger;

import java.util.Collections;
import java.util.Map;

public class DriverMountPoint extends MountPoint{
    private static final Logger LOGGER = LogUtil.getLogger();

    private final Driver driver;

    public DriverMountPoint(BusProxy busProxy, DriverConfig driverConfig) {
        super(driverConfig.path,
              Collections.unmodifiableMap(driverConfig.initParams),
              busProxy);

        this.driver = ObjectUtil.newInstance(driverConfig.driverClassName);
    }

    @Override
    protected Class getImplementationClass() {
        return driver.getClass();
    }
}
