package net.gabert.sdx.heiko.mountpoint;

import net.gabert.sdx.heiko.core.HeikoMessage;
import net.gabert.sdx.kyla.api.Endpoint;
import net.gabert.sdx.kyla.core.BusProxy;

/**
 *
 * @author Robert Gallas
 */
public abstract class HeikoEndpoint extends Endpoint<HeikoMessage> {
    protected HeikoEndpoint(BusProxy busProxy) {
        super(busProxy);
    }
}

