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
    public void register(Endpoint endpoint) {
        endpoints.addIfAbsent(endpoint);
    }

    @Override
    public List<Endpoint> getEndpoints() {
        Endpoint ep = endpoints.get(pointer);

        pointer = (pointer == endpoints.size() - 1) ? 0 : pointer + 1;

        return Arrays.asList(ep);
    }

}
