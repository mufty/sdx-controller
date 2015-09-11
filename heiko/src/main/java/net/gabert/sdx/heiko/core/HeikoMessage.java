package net.gabert.sdx.heiko.core;

/**
 *
 * @author Robert Gallas
 */
public class HeikoMessage<T> {
    public static enum Type {
        GET, SET, SET_ACK, CALL, REPLY
    }

    public String mountPointRelativePath;
    public T payload;
    public Type type;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{type: ").append(type).append(", ");
        sb.append("mountPointRelativePath: ").append(mountPointRelativePath).append(", ");
        sb.append("payload: ").append(payload).append("}");

        return sb.toString();
    }
}
