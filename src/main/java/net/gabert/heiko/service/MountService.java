package net.gabert.heiko.service;

import net.gabert.heiko.driver.AbstractDriver;
import net.gabert.kyla.api.Endpoint;

public interface MountService {

    void register(String mountPoint, AbstractDriver driver);

    void mount(String mountPoint, Endpoint edpoint);

    void mount(String mountPoint, String dataslotId);

    String getMount(String s);

    String getPathSeparator();
}
