package com.kyee.monitor.base.logging;

import java.lang.reflect.Constructor;

public class LogFactory {

    private static Constructor<?> logConstructor;

    static {

        tryImplementation("org.apache.log4j.Logger","com.kyee.monitor.base.logging.Log4j2Impl");
        tryImplementation("org.apache.logging.log4j.Logger","com.kyee.monitor.base.logging.Log4jImpl");

        if (logConstructor == null) {
            try {
                logConstructor = NoLoggingImpl.class.getConstructor(String.class);
            } catch (Exception e) {
                throw new IllegalStateException(e.getMessage(), e);
            }
        }
    }

    private static void tryImplementation(String testClassName, String implClassName) {
        if (logConstructor != null) {
            return;
        }

        try {
            Resources.classForName(testClassName);
            Class<?> implClass = Resources.classForName(implClassName);
            logConstructor = implClass.getConstructor(new Class[] { String.class });

            Class<?> declareClass = logConstructor.getDeclaringClass();
            if (!Log.class.isAssignableFrom(declareClass)) {
                logConstructor = null;
            }

            try {
                if (null != logConstructor) {
                    logConstructor.newInstance(LogFactory.class.getName());
                }
            } catch (Throwable t) {
                logConstructor = null;
            }

        } catch (Throwable t) {

        }
    }

    public static Log getLog(Class<?> clazz) {
        return getLog(clazz.getName());
    }

    public static Log getLog(String loggerName) {
        try {
            return (Log) logConstructor.newInstance(loggerName);
        } catch (Throwable throwable) {
            throw new RuntimeException("Error creating logger for logger '" + loggerName + "'.  Cause: " + throwable, throwable);
        }
    }

}
