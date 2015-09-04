package net.gabert.sdx.heiko.mountpoint;

import net.gabert.sdx.heiko.core.HeikoMessage;
import net.gabert.sdx.kyla.api.Endpoint;
import net.gabert.sdx.kyla.core.BusProxy;

import java.util.Map;

/**
 *
 * @author Robert Gallas
 */
public abstract class MountPoint extends Endpoint<HeikoMessage> {
    private final BusProxy busProxy;

    public MountPoint(BusProxy busProxy) {
        super(busProxy);
        this.busProxy = busProxy;
    }

    public MountPoint(String dataslotId,
                      BusProxy busProxy) {
        super(dataslotId, busProxy);
        this.busProxy = busProxy;
    }

    public void init() {
        busProxy.register(this);
    }

    public abstract void start();
}
