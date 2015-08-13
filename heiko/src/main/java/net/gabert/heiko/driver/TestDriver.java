package net.gabert.heiko.driver;

import net.gabert.heiko.driver.AbstractDriver;

/**
 * Created by Family on 11. 8. 2015.
 */
public class TestDriver extends AbstractDriver {
    public void requestMount(String subPath, String dataslot1) {
        mount(subPath, dataslot1);
    }
}
