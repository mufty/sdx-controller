package net.gabert.sdx.kyla.api;

import net.gabert.sdx.kyla.api.Endpoint.Message;

/**
 * Base interface class enabling sending messages to Bus infrastructure
 */
public interface Bus {

    /**
     * Method sends Message object to communication infrastructure.
     *
     * @param message message to be sent
     */
    void send(Message message);
}
