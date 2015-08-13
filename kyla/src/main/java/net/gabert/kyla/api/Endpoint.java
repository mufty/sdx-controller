package net.gabert.kyla.api;

import net.gabert.kyla.bus.BusProxy;
import net.gabert.kyla.util.Security;

import java.io.Serializable;
import java.util.UUID;

public abstract class Endpoint {
    public static final String ID_CLASSIFIER = "urn:uuid:";

    private final String dataSlotId;
    private final BusProxy busProxy;

    protected Endpoint(BusProxy busProxy) {
        this.busProxy = busProxy;
        this.dataSlotId = ID_CLASSIFIER + Security.getUUID().toString();
    }

    protected final void send(Message message) {
        busProxy.send(message);
    }
    
    public String getDataSlotId() {
        return dataSlotId;
    }

    protected Message createMessage(String destinationKey, Object data) {
        return new Message(destinationKey,
                this.getDataSlotId(),
                Security.getUUID(),
                Security.getUUID(),
                data);
    }

    public abstract void handle(Message message);

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

    /**
     * Structured object representing bus message
     */
    public static class Message implements Serializable {
        private final String destinationSlotId;
        private final String sourceSlotId;
        private final UUID messageId;
        private final UUID conversationId;
        private final Object data;

        private Message(String destinationSlotId,
                        String sourceSlotId,
                        UUID messageId,
                        UUID conversationId,
                        Object data) {
            this.destinationSlotId = destinationSlotId;
            this.sourceSlotId = sourceSlotId;
            this.messageId = Security.getUUID();
            this.conversationId = conversationId;
            this.data = data;
        }

        @Override
        public String toString() {
            return messageId.toString() + " : " + data;
        }

        public String getDestinationSlotId() {
            return destinationSlotId;
        }

        public String getSourceSlotId() {
            return sourceSlotId;
        }

        public UUID getMessageId() {
            return messageId;
        }

        public UUID getConversationId() {
            return conversationId;
        }

        public Object getData() {
            return data;
        }

        public Message createReply(Object data) {
            return new Message(this.sourceSlotId,
                    this.destinationSlotId,
                    Security.getUUID(),
                    this.conversationId,
                    data);
        }
    }
}
