package net.gabert.sdx.kyla.dataslot.local;

import net.gabert.sdx.kyla.api.Endpoint;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Robert Gallas
 */
public class ParallelDataSlot extends DataSlot {
    private int pointer = 0;

    protected ParallelDataSlot(String dataSlotId) {
        super(dataSlotId);
    }

    @Override
    public List<Endpoint> getEndpoints() {
        Endpoint ep = super.getEndpoints().get(pointer);

        pointer = (pointer == super.getEndpoints().size() - 1) ? 0 : pointer + 1;

        return Arrays.asList(ep);
    }
}
