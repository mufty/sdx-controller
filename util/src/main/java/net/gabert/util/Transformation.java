package net.gabert.util;

/**
 *
 * @author Robert Gallas
 */
public interface Transformation<T> {
    T fromString(String fileContent, Class<T> clazz);
    T fromFile(String fileUrl, Class<T> clazz);
}
