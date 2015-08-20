package net.gabert.heiko.mountpoint;

import net.gabert.heiko.service.MountService;
import net.gabert.kyla.api.Endpoint;

public abstract class MountPointProvider {
    private String mountPoint;
    private MountService mountService;

    public synchronized void setMountPoint(MountService mountService, String mountPoint) {
        if ( this.mountService != null ) {
            throw new IllegalStateException("Driver already initialized");
        }

        this.mountService = mountService;
        this.mountPoint = mountPoint;
    }

    protected final void mount(String subPath, Endpoint endpoint) {
        String absolutePath = absolutePath(normalizeSubPath(subPath));
        mountService.mount(absolutePath, endpoint);
    }

    protected final void mount(String subPath, String dataslot) {
        String absolutePath = absolutePath(normalizeSubPath(subPath));
        mountService.mount(absolutePath, dataslot);
    }

    private final String absolutePath(String normalizedSubPath) {
        return mountPoint + mountService.getPathSeparator() + normalizedSubPath;
    }

    private String normalizeSubPath(String subPath) {
        String normalizedPath = subPath.trim();
        if(normalizedPath.startsWith("/")) {
            normalizedPath = normalizedPath.replaceFirst("/", "");
        }

        if(normalizedPath.endsWith("/")) {
            normalizedPath = normalizedPath.substring(normalizedPath.lastIndexOf("/"), normalizedPath.length() - 1);
        }

        return normalizedPath;
    }
}
