package net.gabert.heiko.mountpoint;

/**
 * Created by Family on 11. 8. 2015.
 */
public class TestDriver extends MountPointProvider {
    public void requestMount(String subPath, String dataslot1) {
        mount(subPath, dataslot1);
    }
}
