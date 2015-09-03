package net.gabert.sdx.heiko.core;

/**
 *
 * @author Robert Gallas
 */
public class HeikoMessage<T> {
    public static enum Type {
        GET, SET, CALL, REPLY
    }

    public String absolutePath;
    public T payload;
    public Type type;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{type: ").append(type).append(", ");
        sb.append("absolutePath: ").append(absolutePath).append(", ");
        sb.append("payload: ").append(payload).append("}");

        return sb.toString();
    }
}
