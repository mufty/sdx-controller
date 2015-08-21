package net.gabert.util;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class Log {
    private static final String SOUT_PREFIX = "SYSOUT-> ";
    private static final Map<Class, Logger> LOGGERS = new ConcurrentHashMap<>();

    public static void sout(Object object) {
        System.out.println(SOUT_PREFIX + object);
    }

    public static void wait(String prompt) {
        sout(prompt);
        try {
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Logger logger(Object object) {
        Class toBeLoggedClass = object.getClass();

        if (LOGGERS.containsKey(toBeLoggedClass) == false) {
            LOGGERS.put(toBeLoggedClass, Logger.getLogger(toBeLoggedClass));
        }

        return LOGGERS.get(toBeLoggedClass);
    }
}
