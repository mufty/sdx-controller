package net.gabert.util;

import org.apache.log4j.Logger;

import java.io.IOException;

public final class LogUtil {
    private static final String SOUT_PREFIX = "SYSOUT-> ";

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

    public static Logger getLogger() {
        StackTraceElement caller = Thread.currentThread().getStackTrace()[2];
        return Logger.getLogger(caller.getClassName());
    }
}
