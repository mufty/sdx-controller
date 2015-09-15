package net.gabert.sdx.heiko.core;

import net.gabert.sdx.heiko.mountpoint.ComponentMountPoint;
import net.gabert.util.LogUtil;
import net.gabert.util.PathMap;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Robert Gallas
 */
public class MappingService {
    private static final Logger LOGGER = LogUtil.getLogger();

    private static final String PATH_SEPARATOR = "/";

    private final List<ComponentMountPoint> mountPointRegistry = new ArrayList<>();

    private final PathMap<String> mountPathReducer = new PathMap<>(PATH_SEPARATOR);
    private final Map<String, String> mountPathToDtaSlotId = new HashMap<>();

    public void map(String mountPath, String dataSlotId) {
        mountPathReducer.put(mountPath, mountPath);
        mountPathToDtaSlotId.put(mountPath, dataSlotId);

        LOGGER.info("Mapped: {} -> {}", mountPath, dataSlotId);
    }

    public void link(String existingMountPath, String newMountPath) {
        String dataSlotId = resolveDataSlotId(existingMountPath);
        map(newMountPath, dataSlotId);
    }

    public String resolveDataSlotId(String absolutePath) {
        String mountPointPath = mountPathReducer.get(absolutePath);
        return mountPathToDtaSlotId.get(mountPointPath);
    }

    public String getMountPointRelativePath(String absolutePath) {
        String mountPointPath = mountPathReducer.get(absolutePath);
        return absolutePath.replace(mountPointPath, "");
    }
}
