package net.gabert.sdx.sampleapp.driver;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Robert Gallas
 */
public class SimpleValueHolder {
    private Map<String, Object> map = new HashMap<>();

    public SimpleValueHolder(String... keys) {
        for (String key : keys) {
            map.put(key, null);
        }
    }

    public void setValue(String key, Object value) {
        map.replace(key, value);
    }

    public Object getValue(String key) {
        return map.get(key);
    }
}
