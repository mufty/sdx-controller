package net.gabert.sdx.heiko.mountpoint;

import net.gabert.sdx.heiko.configuration.schema.DriverConfig;
import net.gabert.sdx.heiko.configuration.schema.ServiceConfig;
import net.gabert.sdx.kyla.core.BusProxy;
import net.gabert.util.LogUtil;
import net.gabert.util.ObjectUtil;
import net.gabert.util.PathMap;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Robert Gallas
 */
public class MountServiceLocal implements MountService {
    private static final Logger LOGGER = LogUtil.getLogger();

    private static final String PATH_SEPARATOR = "/";

    private final List<MountPoint> mountPointRegistry = new ArrayList<>();

    private final PathMap<MountPoint> pathMap = new PathMap<>(PATH_SEPARATOR);

    private final BusProxy busProxy;

    public MountServiceLocal(BusProxy busProxy) {
        this.busProxy = busProxy;
        LOGGER.info(this.getClass().getSimpleName() + " initialized.");
    }

    @Override
    public void mount(DriverConfig driverConfig)  {
        try {
            LOGGER.info("Initializing driver: " + driverConfig.path);
            DriverMountPoint driverMountPoint = new DriverMountPoint(busProxy, driverConfig);
            mount(driverMountPoint);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void mount(ServiceConfig serviceConfig) {
        try {
            LOGGER.info("Initializing service: " + serviceConfig.path);
            ServiceMountPoint serviceMountPoint = new ServiceMountPoint(busProxy, serviceConfig);
            mount(serviceMountPoint);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void mount(MountPoint mountPoint) {
        pathMap.put(mountPoint.getMountPointContextRoot(), mountPoint);
        mountPointRegistry.add(mountPoint);
        mountPoint.init();
        LOGGER.info("Mounted: " + mountPoint);
    }

    @Override
    public MountPoint getMountPoint(String requestPath) {
        return pathMap.get(requestPath);
    }
}
