package net.gabert.heiko.service;

import net.gabert.heiko.mountpoint.MountPointProvider;
import net.gabert.kyla.api.Endpoint;

public interface MountService {

    void register(String mountPoint, MountPointProvider driver);

    void mount(String mountPoint, Endpoint edpoint);

    void mount(String mountPoint, String dataslotId);

    String getMount(String s);

    String getPathSeparator();
}
