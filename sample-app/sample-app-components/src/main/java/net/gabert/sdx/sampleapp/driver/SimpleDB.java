package net.gabert.sdx.sampleapp.driver;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Robert Gallas
 */
public class SimpleDB {
    private Map<String, Object> map = new HashMap<>();

    public SimpleDB(String... keys) {
        for (String key : keys) {
            map.put(key, null);
        }
    }

    public synchronized void setValue(String key, Object value) {
        map.replace(key, value);
    }

    public synchronized Object getValue(String key) {
        return map.get(key);
    }
}
