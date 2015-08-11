package net.gabert.heiko.core;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by Family on 11. 8. 2015.
 */
public class BusTest {
    private Bus bus = new Bus();

    @Before
    public void init() {
        DummyDriver driver = new DummyDriver();

        bus.mount("/a/b/c", driver);
    }

    @Test
    public void setValueToPath() {
        bus.set("/a/b/c/d", "VAL1");
    }
}
