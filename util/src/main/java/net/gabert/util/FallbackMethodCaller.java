package net.gabert.util;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.apache.log4j.Logger;

public class FallbackMethodCaller implements InvocationHandler {
    private static final Logger LOGGER = Logger.getLogger(FallbackMethodCaller.class);

    private final Object source;
    private final Object fallback;

    private FallbackMethodCaller(Object source, Object fallback) {
        this.source = source;
        this.fallback = fallback;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        try {
            return method.invoke(source, args);
        } catch (Throwable t){
            LOGGER.warn("Failed to call method " +
                        method.getName() +
                        " on " +
                        source.getClass().getSimpleName() +
                        ". Will call " +
                        fallback.getClass().getSimpleName()+".");
            return method.invoke(fallback, args);
        }
    }

    public static <T> T getInstance(Class<T> interfaceClass, T source, T fallback) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(),
                                          new Class[]{interfaceClass},
                                          new FallbackMethodCaller(source, fallback));
    }
}
