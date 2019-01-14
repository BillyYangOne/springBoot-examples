package com.kyee.monitor.base.logging;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Log4jImpl implements Log {

    private static final String callerFQCN = Log4jImpl.class.getName();

    private Logger log ;

    public Log4jImpl(Logger logger){
        this.log = logger;
    }

    public Log4jImpl(String className){
        this.log = Logger.getLogger(className);
    }

    @Override
    public boolean isDebugEnabled() {
       return log.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        log.log(callerFQCN, Level.DEBUG, msg, null);
    }

    @Override
    public void debug(String msg, Throwable e) {
        log.log(callerFQCN, Level.DEBUG, msg, e);
    }

    @Override
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        log.log(callerFQCN, Level.INFO, msg, null);
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isEnabledFor(Level.WARN);
    }

    @Override
    public void warn(String msg) {
        log.log(callerFQCN, Level.WARN, msg, null);
    }

    @Override
    public void warn(String msg, Throwable e) {
        log.log(callerFQCN, Level.WARN, msg, e);
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isEnabledFor(Level.ERROR);
    }
    @Override
    public void error(String msg, Throwable e) {
        log.log(callerFQCN, Level.ERROR, msg, e);

    }

    @Override
    public void error(String msg) {
        log.log(callerFQCN, Level.ERROR, msg, null);
    }

    @Override
    public void error(Throwable e) {
        log.log(callerFQCN, Level.ERROR,"", null);
    }


}
