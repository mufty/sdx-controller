package net.gabert.kyla.bus;

import net.gabert.kyla.api.Bus;
import net.gabert.kyla.api.DataSlotProvider;
import net.gabert.kyla.api.Endpoint;

public class SimpleBus implements Bus {
    private final DataSlotProvider dataSlotProvider;

    public SimpleBus(DataSlotProvider dataSlotProvider) {
        this.dataSlotProvider = dataSlotProvider;
    }

    @Override
    public void send(Endpoint.Message message) {
        dataSlotProvider.distribute(message, message.getDestinationSlotId());
    }
}
