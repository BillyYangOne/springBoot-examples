package com.kyee.monitor.base.event.internal;

import com.kyee.monitor.base.component.BaseComponent;
import com.kyee.monitor.base.event.BaseEvent;
import com.kyee.monitor.base.event.FusingMonitorEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;


@Component
public class FusingExecutionHandler extends BaseComponent implements RejectedExecutionHandler {

    private AtomicInteger counter = new AtomicInteger(0);

//    private int limit = 100;

    private String openFusing = "";
    @PostConstruct
    private void init(){
       openFusing = getConfigurationService().getApplicationCfg().getProperty("options.monitor.eventFusing","off");
       logger.info("初始化监控熔断开关，options.monitor.eventFusing="+openFusing);
    }



    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        int currentCounter = counter.incrementAndGet();
        logger.error("监控线程池已满，当前拒绝任务数量currentCounter ="+currentCounter);
        if ("on".equalsIgnoreCase(openFusing)) {
            logger.error("监控线程池触发熔断");
            publishFusingEvent(new FusingMonitorEvent("ThreadPoolExecutor is full!"));
            clearCounter();
        }

    }

    private void publishFusingEvent(BaseEvent baseEvent) {
        publishEvent(baseEvent);
    }

    private void clearCounter(){
        counter.set(0);
    }
}
