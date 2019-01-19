package com.kyee.monitor.base.logging;

public class NoLoggingImpl implements Log {

    private String loggerName;

    private boolean debugEnable = false;
    private boolean infoEnable = true;
    private boolean warnEnable = true;
    private boolean errorEnable = true;

    public NoLoggingImpl(String loggerName){
        this.loggerName = loggerName;
    }

    @Override
    public boolean isDebugEnabled() {
        return debugEnable;
    }

    @Override
    public void debug(String msg) {

    }

    @Override
    public void debug(String msg, Throwable e) {

    }

    @Override
    public boolean isInfoEnabled() {
        return infoEnable;
    }

    @Override
    public void info(String msg) {

    }

    @Override
    public boolean isWarnEnabled() {
        return warnEnable;
    }

    @Override
    public void warn(String msg) {
        System.out.println(loggerName + " : " + msg);
    }

    @Override
    public void warn(String msg, Throwable e) {
        System.out.println(loggerName + " : " + msg);
    }

    @Override
    public boolean isErrorEnabled() {
        return errorEnable;
    }

    @Override
    public void error(String msg, Throwable e) {

        System.err.println(loggerName + " : " + msg);
    }

    @Override
    public void error(String msg) {
        System.err.println(loggerName + " : " + msg);
    }

    @Override
    public void error(Throwable e) {

    }
}
