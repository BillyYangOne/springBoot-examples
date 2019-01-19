package com.kyee.monitor.core.listener;

import com.kyee.monitor.core.listener.def.IEventListener;
import com.kyee.monitor.core.listener.def.SystemEventDriver;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("listenerConfig")
@Lazy
public class ListenerConfig implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    @Autowired
    @Qualifier("requestMonitorListener")
    private RequestMonitorListener requestMonitorListener;

    @Autowired
    private FusingMonitorListener fusingMonitorListener;

    public void listenerConfiguration() {
        List<IEventListener> listeners = new ArrayList<>();
        listeners.add(requestMonitorListener);
        listeners.add(fusingMonitorListener);
        ((SystemEventDriver)applicationContext.getBean("systemEventDriver")).addListeners(listeners);

    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ListenerConfig.applicationContext = applicationContext;
    }

    /**
     * @describe 注销监听器
     */
    public void destroyListeners(){
        ((SystemEventDriver)applicationContext.getBean("systemEventDriver")).destroyListeners();
    }
}
