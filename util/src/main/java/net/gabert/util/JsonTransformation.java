package net.gabert.util;

public class JsonTransformation<T> implements Transformation<T> {

    @Override
    public T fromString(String fileContent, Class<T> clazz) {
        return JsonFileReader.fromString(fileContent, clazz);
    }

    @Override
    public T fromFile(String fileUrl, Class<T> clazz) {
        return JsonFileReader.fromFile(fileUrl, clazz);
    }
}
