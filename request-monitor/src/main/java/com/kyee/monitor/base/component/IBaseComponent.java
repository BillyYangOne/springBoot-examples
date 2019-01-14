package com.kyee.monitor.base.component;

import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ServletContextAware;

import com.kyee.monitor.base.config.IConfigurationService;
import com.kyee.monitor.base.event.BaseEvent;



public interface IBaseComponent extends ApplicationContextAware, ServletContextAware  {
	
	
	/**
	 * 发布事件
	 * 
	 * @param event
	 */
	void publishEvent(BaseEvent event);

	/**
	 * 获取配置服务
	 * 
	 * @return
	 */
	IConfigurationService getConfigurationService();
	

}
