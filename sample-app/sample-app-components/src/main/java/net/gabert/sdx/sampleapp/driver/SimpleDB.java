package net.gabert.sdx.sampleapp.driver;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Robert Gallas
 */
public class SimpleDB {
    private Map<String, Object> map = new ConcurrentHashMap<>();

    public SimpleDB(String... keys) {
        for (String key : keys) {
            map.put(key, new Object());
        }
    }

    public void setValue(String key, Object value) {
        if (map.containsKey(key)) {
            map.put(key, value);
        }
    }

    public Object getValue(String key) {
        return map.get(key);
    }
}
