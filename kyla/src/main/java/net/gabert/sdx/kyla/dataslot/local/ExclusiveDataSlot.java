package net.gabert.sdx.kyla.dataslot.local;

import net.gabert.sdx.kyla.api.Endpoint;

/**
 *
 * @author Robert Gallas
 */
public class ExclusiveDataSlot extends DataSlot {

    public ExclusiveDataSlot(Endpoint endpoint, String dataSlotID) {
        super(dataSlotID);
        super.register(endpoint);
    }

    @Override
    public void register(Endpoint endpoint) {
        throw new IllegalArgumentException("Registration of ["+endpoint+"] not allowed to ExclusiveDataSlot " + this);
    }
}
