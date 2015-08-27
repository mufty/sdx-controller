package net.gabert.util;

public final class ObjectFactory {
    public static <T> T newInstance(String objectClassName) {
        try {
            Class<?> clazz = Class.forName(objectClassName);
            return (T) clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
