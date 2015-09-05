package net.gabert.sdx.kyla.api;

import net.gabert.util.Security;

import java.io.Serializable;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Robert Gallas
 */
public abstract class Endpoint<T> {
    public static final String ID_CLASSIFIER = "urn:uuid:";

    private final String dataSlotId;
    private final Bus bus;

    protected Endpoint(Bus bus) {
        this(getDefaultDataSlotId(), bus);
    }

    protected Endpoint(String dataSlotId, Bus bus) {
        this.bus = bus;
        this.dataSlotId = dataSlotId.startsWith(ID_CLASSIFIER) ? dataSlotId
                                                               : attachClassifier(dataSlotId);
    }

    private static String getDefaultDataSlotId() {
        return attachClassifier(Security.getUUID().toString());
    }

    private static String attachClassifier(String dataSlotId) {
        return ID_CLASSIFIER + dataSlotId;
    }

    public void send(Message<T> message) {
        bus.send(message);
    }
    
    public String getDataSlotId() {
        return dataSlotId;
    }

    public String getPlainDataSlotId() {
        return dataSlotId.replace(ID_CLASSIFIER, "");
    }

    public abstract void handle(Message<T> message);

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Endpoint endpoint = (Endpoint) o;

        if (dataSlotId != null ? !dataSlotId.equals(endpoint.dataSlotId) : endpoint.dataSlotId != null) return false;

        return true;
    }

    @Override
    public String toString() {
        return getDataSlotId();
    }

    @Override
    public int hashCode() {
        return dataSlotId != null ? dataSlotId.hashCode() : 0;
    }

    private static final AtomicInteger INT_ID_GENERATOR = new AtomicInteger();

    public <T> Message<T> createMessage(String destinationSlotId, T data) {
        return new Message(destinationSlotId,
                           this.getDataSlotId(),
                           INT_ID_GENERATOR.incrementAndGet(),
                           data);
   }

    /**
     * Structured object representing bus message
     */
    public static class Message<T> implements Serializable {
        private final String destinationSlotId;
        private final String sourceSlotId;
        private final long conversationId;
        private final T data;

        private Message(String destinationSlotId,
                        String sourceSlotId,
                        long conversationId,
                        T data) {
            this.destinationSlotId = destinationSlotId;
            this.sourceSlotId = sourceSlotId;
            this.conversationId = conversationId;
            this.data = data;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("conversationId: ").append(String.valueOf(conversationId)).append(", ");
            sb.append("sourceSlotId: ").append(sourceSlotId).append(", ");
            sb.append("destinationSlotId: ").append(destinationSlotId.toString()).append(", ");
            sb.append("data: ").append(data).append("}");

            return sb.toString();
        }

        public String getDestinationSlotId() {
            return destinationSlotId;
        }

        public String getSourceSlotId() {
            return sourceSlotId;
        }

        public long getConversationId() {
            return conversationId;
        }

        public T getData() {
            return data;
        }

        public Message createReply(T data) {
            return new Message(this.sourceSlotId,
                               this.destinationSlotId,
                               this.conversationId,
                               data);
        }
    }
}
