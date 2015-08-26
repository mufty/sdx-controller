package net.gabert.sdx.heiko.mountpoint;

import net.gabert.sdx.heiko.configuration.schema.MountPointConfig;
import net.gabert.sdx.kyla.core.BusProxy;
import net.gabert.util.LogUtil;
import net.gabert.util.PathMap;
import org.apache.log4j.Logger;

import java.util.List;

public class MountServiceLocal implements MountService {
    private static final Logger LOGGER = LogUtil.getLogger();

    private static final String PATH_SEPARATOR = "/";

    private final PathMap<MountPoint> pathMap = new PathMap<>(PATH_SEPARATOR);

    private final BusProxy busProxy;

    public MountServiceLocal(BusProxy busProxy) {
        this.busProxy = busProxy;
        LOGGER.info(this.getClass().getSimpleName() + " initialized.");
    }

    @Override
    public void mount(List<MountPointConfig> mountPointConfigs) {
        for (MountPointConfig config : mountPointConfigs) {
            mount(config);
        }
    }

    @Override
    public void mount(MountPointConfig mountPointConfig)  {
        try {
            LOGGER.info("Initializing mountpoint: " + mountPointConfig.path);
            MountPoint mountPoint = new MountPoint(busProxy, mountPointConfig);
            mount(mountPoint);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void mount(MountPoint mountPoint) {
        pathMap.put(mountPoint.getMountPointContextRoot(), mountPoint);
        LOGGER.info("Mounted: " + mountPoint);
    }

    @Override
    public MountPoint getMountPoint(String requestPath) {
        return pathMap.get(requestPath);
    }
}
