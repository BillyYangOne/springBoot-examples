package com.kyee.monitor.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kyee.monitor.base.component.BaseComponent;
import com.kyee.monitor.base.resultmodel.ResultModel;
import com.kyee.monitor.base.resultmodel.StandardResultModel;
import com.kyee.monitor.core.boot.IBootService;
import com.kyee.monitor.core.listener.def.EventListenerRegistry;
import com.kyee.monitor.core.listener.def.SystemEventDriver;

@Controller
@RequestMapping("monitorStatusController")
public class MonitorStatusController extends BaseComponent {

    @Autowired
    @Qualifier("monitorBootService")
    private IBootService bootService;


    @RequestMapping("getMonitorListeners")
    public ResultModel getMonitorListeners(){
    	
        ApplicationContext context = getApplicationContext();
        if (context != null) {
            EventListenerRegistry systemEventDriver = ((SystemEventDriver) context.getBean("systemEventDriver")).getRegistry();
            return new StandardResultModel(systemEventDriver.getListeners().toString());
        }else {
            return new StandardResultModel("context为空");
        }
    }

    @RequestMapping("pauseAgent")
    public ResultModel pauseAgent(){
        bootService.pause();
        return new StandardResultModel(true);
    }

    @RequestMapping("resumeAgent")
    public ResultModel resumeAgent(){
        bootService.resume();
        return new StandardResultModel(true);
    }


}
