package net.gabert.kyla.test.stub;

import net.gabert.kyla.api.KylaConfiguration;
import net.gabert.kyla.api.Endpoint;
import net.gabert.kyla.bus.BusProxy;

public class BusProxyStub extends BusProxy {
    public BusProxyStub(KylaConfiguration kylaConfiguration) {
        super(kylaConfiguration);
    }

    public boolean isEndpointRegistered(Endpoint endpoint) {
        return  isEndpointRegistered(endpoint, endpoint.getDataSlotId());
    }
    
    public boolean isEndpointRegistered(Endpoint endpoint, String dataSlotId) {
        return     dataSlotProvider.slotExists(dataSlotId)
                && dataSlotProvider.endpointRegistered(dataSlotId, endpoint);
    }
}
