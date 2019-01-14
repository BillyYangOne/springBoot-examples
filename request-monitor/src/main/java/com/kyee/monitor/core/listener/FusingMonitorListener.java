package com.kyee.monitor.core.listener;

import com.kyee.monitor.base.event.BaseEvent;
import com.kyee.monitor.base.event.BaseEventListener;
import com.kyee.monitor.core.listener.def.ListenerChain;
import com.kyee.monitor.core.listener.def.SystemEventDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

@Component
public class FusingMonitorListener extends BaseEventListener {

    @Autowired
    private SystemEventDriver systemEventDriver;

    @PostConstruct
    public void init() {

        setListen("_FusingMonitor");
    }

    @Override
    public void onFire(BaseEvent event, ListenerChain chain, ApplicationContext applicationContext, ServletContext servletContext) throws Exception {
        logger.info("停止监控事件的监听:"+event.getSource().toString());
        systemEventDriver.destroyListeners();
    }
}
