package net.gabert.sdx.heiko.mountpoint;

import net.gabert.sdx.heiko.component.Service;
import net.gabert.sdx.heiko.core.HeikoMessage;
import net.gabert.sdx.kyla.api.Endpoint;
import net.gabert.sdx.kyla.core.BusProxy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Robert Gallas
 */
public abstract class MountPoint extends Endpoint<HeikoMessage> {
    private final BusProxy busProxy;

    private final Map<Long, Service.Callback> pendingResponses = new ConcurrentHashMap<>();


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

    public void send(Message kylaMessage, Service.Callback callback) {
        pendingResponses.put(kylaMessage.getConversationId(), callback);
        send(kylaMessage);
    }

    protected void possiblyHandleCallback(Message kylaMessage) {
        long conversationId = kylaMessage.getConversationId();
        if (pendingResponses.containsKey(conversationId)) {
            HeikoMessage heikoMessage = (HeikoMessage) kylaMessage.getData();
            Service.Callback callback = pendingResponses.remove(conversationId);
            callback.done(heikoMessage.payload);
        }
    }

    public abstract void start();
}
