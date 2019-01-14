package com.kyee.monitor.core.listener;


import com.kyee.monitor.base.event.BaseEvent;
import com.kyee.monitor.base.event.BaseEventListener;
import com.kyee.monitor.core.listener.def.ListenerChain;
import com.kyee.monitor.core.recorder.RequestMonitorSave;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
@Component
public class RequestMonitorListener extends BaseEventListener {
	
	@Autowired
	@Qualifier("requestMonitorSave")
	private RequestMonitorSave requestMonitorSave;

	@PostConstruct
	public void init() {

		setListen("_MONITOR");
	}

	@Override
	public void onFire(BaseEvent event, ListenerChain chain, ApplicationContext applicationContext,
					   ServletContext servletContext) throws Exception {
		requestMonitorSave.saveRequestMonitorData(event.getSource());
		chain.stop();
	}

}
