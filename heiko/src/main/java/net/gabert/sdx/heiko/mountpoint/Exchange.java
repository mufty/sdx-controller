package net.gabert.sdx.heiko.mountpoint;

import net.gabert.sdx.heiko.api.Service;
import net.gabert.sdx.heiko.core.HeikoMessage;
import net.gabert.sdx.kyla.api.Endpoint;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author Robert Gallas
 */
public class Exchange {
    private static final Map<Long, Exchange> PENDING_RPC_RESPONSES = new ConcurrentHashMap<>();

    private HeikoMessage heikoResponse;

    private Service.Callback callback;

    private final CountDownLatch latch = new CountDownLatch(1);

    private Exchange(Service.Callback callback) {
        this.callback = callback;
    }

    public HeikoMessage getResponse() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        synchronized (this) {
            return this.heikoResponse;
        }
    }

    private void setResponse(Endpoint.Message kylaResponse) {
        this.heikoResponse = (HeikoMessage)kylaResponse.getData();
        latch.countDown();

        if (callback != null) {
            callback.done(this.heikoResponse.payload);
        }
    }

    public static Exchange createExchange(Endpoint.Message kylaMessage) {
        Exchange exchange = new Exchange(null);
        PENDING_RPC_RESPONSES.put(kylaMessage.getConversationId(), exchange);

        return exchange;
    }

    public static Exchange createExchange(Endpoint.Message kylaMessage, Service.Callback callback) {
        Exchange exchange = new Exchange(callback);
        PENDING_RPC_RESPONSES.put(kylaMessage.getConversationId(), exchange);

        return exchange;
    }

    public static void possiblySetResponse(Endpoint.Message possibleKylaResponse) {
        Exchange exchange = PENDING_RPC_RESPONSES.remove(possibleKylaResponse.getConversationId());
        if (exchange != null) {
            exchange.setResponse(possibleKylaResponse);
        }
    }
}
