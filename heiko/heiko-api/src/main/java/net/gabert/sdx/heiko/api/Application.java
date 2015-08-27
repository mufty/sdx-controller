package net.gabert.sdx.heiko.api;

import net.gabert.sdx.kyla.api.Endpoint;
import net.gabert.sdx.kyla.core.BusProxy;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;

public abstract class Application {
    private final Sender sender;

    private BusProxy busProxy;

    protected Application() {
        this.sender = new Sender();
    }

    public abstract void init(Map<String, Object> initParams);

    protected Context getContext(String contextRoot) {
        return new Context(contextRoot);
    }

    protected class Context {
        private final String contextroot;

        private Context(String contextRoot) {
            this.contextroot = contextRoot;
        }

        public void setValue(String path, Object value) {
            Endpoint.Message msg = sender.createMessage(null, null);
            sender.send(msg);
        }

        public Object getValue(String path) {
            Endpoint.Message request = sender.createMessage(null, null);
            return sender.awaitResponse(request);
        }

        protected void setListener(String path, ValueListener value) {

        }
    }

    private class Sender extends Endpoint {
        private final Map<UUID, Exchange> pendingResponses = new ConcurrentHashMap<>();

        public Sender() {
            super(busProxy);
        }

        public Message awaitResponse(Message request) {
            Exchange exchange = new Exchange();
            pendingResponses.put(request.getConversationId(), exchange);
            send(request);
            return exchange.getResponse();
        }

        @Override
        public void handle(Message message) {
            Exchange exchange = pendingResponses.remove(message.getConversationId());
            if (exchange != null) {
                exchange.setResponse(message);
            }
        }
    }

    private static class Exchange {
        private Endpoint.Message response;
        private final CountDownLatch latch = new CountDownLatch(1);

        public Endpoint.Message getResponse() {
            try {
                latch.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            synchronized (this) {
                return response;
            }
        }

        public synchronized void setResponse(Endpoint.Message response) {
            this.response = response;
            latch.countDown();
        }
    }
}
