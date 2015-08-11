package net.gabert.heiko.core;

import net.gabert.kyla.api.Endpoint;

/**
 * Created by Family on 11. 8. 2015.
 */
public interface MountService {

    void mount(String mountPoint, AbstractDriver driver);

    void mount(String mountPoint, Endpoint edpoint);
}
