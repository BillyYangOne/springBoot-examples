package com.kyee.monitor.base.logging;

public interface Log {

    boolean isDebugEnabled();

    void debug(String msg);

    void debug(String msg, Throwable e);

    boolean isInfoEnabled();

    void info(String msg);

    boolean isWarnEnabled();

    void warn(String msg);

    void warn(String msg, Throwable e);

    boolean isErrorEnabled();

    void error(String msg, Throwable e);

    void error(String msg);

    void error(Throwable e);


}
