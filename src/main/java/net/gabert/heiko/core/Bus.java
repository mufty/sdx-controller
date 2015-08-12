package net.gabert.heiko.core;

import net.gabert.heiko.service.MountService;
import net.gabert.heiko.service.MountServiceLocal;

import java.util.HashMap;
import java.util.Map;

public class Bus {
    private MountService mountService = new MountServiceLocal();
    private final Map<Class<?>, Object> serviceRegistry = new HashMap<>();

    public Bus() {
        serviceRegistry.put(MountService.class, new MountServiceLocal());
    }

    public <T> T getService(Class<? extends T> serviceClass) {
        return (T) serviceRegistry.get(serviceClass);
    }

    public void setValue(String path, Object val1) {

    }

    public Object getValue(String path) {
        return null;
    }

    public void setListener(String path, Object listener) {

    }
}
