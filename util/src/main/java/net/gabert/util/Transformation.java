package net.gabert.util;

public interface Transformation<T> {
    T fromFile(String fileUrl);
}
