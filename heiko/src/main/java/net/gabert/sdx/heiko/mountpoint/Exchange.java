package net.gabert.sdx.heiko.mountpoint;

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
    private static final Map<UUID, Exchange> PENDING_RPC_RESPONSES = new ConcurrentHashMap<>();

    private HeikoMessage heikoResponse;
    private final CountDownLatch latch = new CountDownLatch(1);

    public HeikoMessage getResponse() {
        try {
            latch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        synchronized (this) {
            return heikoResponse;
        }
    }

    private synchronized void setResponse(Endpoint.Message kylaResponse) {
        heikoResponse = (HeikoMessage)kylaResponse.getData();
        latch.countDown();
    }

    public static Exchange createExchange(Endpoint.Message kylaMessage) {
        Exchange exchange = new Exchange();
        PENDING_RPC_RESPONSES.put(kylaMessage.getConversationId(), exchange);

        return  exchange;
    }

    public static void possiblySetResponse(Endpoint.Message possibleKylaResponse) {
        Exchange exchange = PENDING_RPC_RESPONSES.remove(possibleKylaResponse.getConversationId());
        if (exchange != null) {
            exchange.setResponse(possibleKylaResponse);
        }
    }
}
