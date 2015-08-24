package net.gabert.util;

public class JsonTransformation<T> implements Transformation<T>{
    @Override
    public T fromFile(String fileUrl) {
        JsonFileReader<T> jsonFileReader = new JsonFileReader(fileUrl);
        return jsonFileReader.parse(null);
    }
}
