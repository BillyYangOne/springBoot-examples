package com.kyee.monitor.base.component;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;

import com.kyee.monitor.base.config.IConfigurationService;
import com.kyee.monitor.base.event.BaseEvent;
import com.kyee.monitor.base.logging.Log;
import com.kyee.monitor.base.logging.LogFactory;

public class BaseComponent implements IBaseComponent{
	
	protected  Log logger = LogFactory.getLog(getClass());
	
	protected ApplicationContext applicationContext;
	
	protected ServletContext servletContext;
	
	//使用 required=false，因为 controllerMonitor 也是此类的子类，
	//而 controllerMonitor 包含在 server.xml 中，前端项目可能仅引用 websupport 而不包含 core，
	//因此不能强制注入
	
	@Autowired(required=false)
	@Qualifier("configurationService")
	protected IConfigurationService configurationService;
	
	@Override
	public void setApplicationContext(ApplicationContext application) throws BeansException {
        this.applicationContext=application;		
	}
	
	public ApplicationContext getApplicationContext(){
		return applicationContext;
	}
	
	public ServletContext getServletContext(){
		return servletContext;
	}
	
	
	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext=servletContext;
		
	}

	@Override
	public void publishEvent(BaseEvent event) {
        this.applicationContext.publishEvent(event);		
	}

	@Override
	public  IConfigurationService getConfigurationService() {
		return this.configurationService;
	}


}
