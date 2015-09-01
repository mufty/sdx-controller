package net.gabert.util;

import java.util.*;

public class PathMap<V> implements Map<String, V> {
    private static class PathElement<V> {
        private final String elementName;
        private V value;
        private final Map<String, PathElement<V>> pathTree = new HashMap<>();

        private PathElement(String elementName) {
            this.elementName = elementName;
        }

        private void append(String[] path, V value) {
            String nextPath = path[0];
            if (!pathTree.containsKey(nextPath)) {
                PathElement pa = new PathElement(nextPath);
                pathTree.put(pa.elementName, pa);
            }

            PathElement nextElement = pathTree.get(nextPath);
            if (path.length == 1) {
                nextElement.value = value;
            } else {
                String[] next = Arrays.copyOfRange(path, 1, path.length);
                nextElement.append(next, value);
            }
        }

        public V getValue(String[] path) {
            String nextPath = path[0];

            if (!pathTree.containsKey(nextPath)) { return null; }

            PathElement nextElement = pathTree.get(nextPath);

            V value = (V) nextElement.value;

            if (path.length > 1){
                String[] next = Arrays.copyOfRange(path, 1, path.length);
                V nextValue = (V) nextElement.getValue(next);
                value = (nextValue == null ) ? value :  nextValue;
            }

            return value;
        }
    }

    private static final String DEFAULT_PATH_DELIMITER = "/";
    private final PathElement rootElement = new PathElement("ROOT");
    private final String pathDelimiter;

    public PathMap() {
        this(DEFAULT_PATH_DELIMITER);
    }

    public PathMap(String pathDelimiter) {
        this.pathDelimiter = pathDelimiter;
    }

    @Override
    public V put(String key, V value) {
        String[] path = pathExplode(key);

        rootElement.append(path, value);

        return value;
    }

    @Override
    public V get(Object key) {
        String[] path = pathExplode((String)key);

        return (V) rootElement.getValue(path);
    }

    private String[] pathExplode(String path) {
        String normalizedPath = path.trim();
        if (normalizedPath.startsWith("/")) {
            normalizedPath = normalizedPath.replaceFirst("/", "");
        }

        if (normalizedPath.endsWith("/")) {
            normalizedPath = normalizedPath.substring(0, normalizedPath.length() - 1);
        }

        return normalizedPath.split(pathDelimiter);
    }

    @Override
    public int size() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isEmpty() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsKey(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public V remove(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void putAll(Map<? extends String, ? extends V> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<String> keySet() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<Entry<String, V>> entrySet() {
        throw new UnsupportedOperationException();
    }
}
