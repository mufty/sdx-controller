package net.gabert.util;

import java.lang.reflect.Field;

/**
 *
 * @author Robert Gallas
 */
public final class ObjectUtil {
    public static <T> T newInstance(String objectClassName) {
        try {
            Class<?> clazz = Class.forName(objectClassName);
            return (T) clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> void injectByValue(Object target, Object value) {
        setValue(getFieldByClassName(target, value.getClass()),
                 target,
                 value);
    }

    private static <T> Field getFieldByClassName(Object instance, Class<T> classToLookup) {
        Class<?> currentClass = instance.getClass();

        while (currentClass.equals(Object.class) == false) {

            for (Field field : currentClass.getDeclaredFields()) {
                if (field.getType().isAssignableFrom(classToLookup)) {
                    return field;
                }
            }

            currentClass = currentClass.getSuperclass();
        }

        return null;
    }

    private static void setValue(Field field, Object target, Object value) {
        boolean accessibleState = field.isAccessible();
        field.setAccessible(true);

        try {
            field.set(target, value);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        field.setAccessible(accessibleState);
    }
}
