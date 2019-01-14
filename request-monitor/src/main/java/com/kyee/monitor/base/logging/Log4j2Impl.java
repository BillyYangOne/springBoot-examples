package com.kyee.monitor.base.logging;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Log4j2Impl implements Log {

    private Logger log;


    public Log4j2Impl(Logger log){
        this.log = log;
    }

    public Log4j2Impl(String loggerName){
        log = LogManager.getLogger(loggerName);
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public void debug(String msg) {
        log.debug(msg);
    }

    @Override
    public void debug(String msg, Throwable e) {
        log.debug(msg,e);
    }

    @Override
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    @Override
    public void info(String msg) {
        log.info(msg);
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    @Override
    public void warn(String msg) {
        log.warn(msg);
    }

    @Override
    public void warn(String msg, Throwable e) {
        log.warn(msg,e);
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    @Override
    public void error(String msg, Throwable e) {
        log.error(msg, e);
    }

    @Override
    public void error(String msg) {
        log.error(msg);
    }

    @Override
    public void error(Throwable e) {
        log.error(e);
    }
}
