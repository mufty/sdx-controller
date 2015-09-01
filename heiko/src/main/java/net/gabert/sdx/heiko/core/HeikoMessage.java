package net.gabert.sdx.heiko.core;

public class HeikoMessage<T> {
    public static enum Type {
        GET, SET, CALL
    }

    public String absolutePath;
    public T payload;
    public Type type;
}
