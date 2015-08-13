package net.gabert.heiko.core;

import net.gabert.heiko.driver.TestDriver;
import net.gabert.heiko.service.MountService;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MountServiceLocalTest {
    private Bus bus;
    private TestDriver driver;

    private static enum MountFixture {
        F1("/d/e", "DATASLOT1"),
        F2("x/y", "DATASLOT2"),
        F3("", "DATASLOT3");

        MountFixture(String subPath, String dataSlotId) {
            this.subPath = subPath;
            this.dataSlotId = dataSlotId;
            this.absolutePath = BASE_PATH + (subPath.startsWith("/") ? "" : "/") + subPath;
        }

        private static final String BASE_PATH = "/a/b/c";
        private String subPath;
        private String absolutePath;
        private String dataSlotId;
    }

    @Before
    public void init() {
        this.bus = new Bus();
        this.driver = new TestDriver();

        MountService mountService = bus.getService(MountService.class);
        mountService.register(MountFixture.BASE_PATH, this.driver);
    }

    @Test
    public void mountTest() {
        setupTestData();
        assertTestData();
    }

    private void setupTestData() {
        for (MountFixture mf :  MountFixture.values()) {
            setUpFixture(mf);
        }
    }

    private void assertTestData() {
        for (MountFixture mf :  MountFixture.values()) {
            assertFixture(mf);
        }
    }

    private void setUpFixture(MountFixture mf) {
        this.driver.requestMount(mf.subPath, mf.dataSlotId);
    }

    private void assertFixture(MountFixture mf) {
        MountService mountService = this.bus.getService(MountService.class);
        assertEquals(mf.dataSlotId, mountService.getMount(mf.absolutePath));
    }
}
