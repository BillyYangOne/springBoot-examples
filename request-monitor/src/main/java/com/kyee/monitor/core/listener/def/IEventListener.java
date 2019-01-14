package com.kyee.monitor.core.listener.def;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;

import com.kyee.monitor.base.component.IBaseComponent;
import com.kyee.monitor.base.event.BaseEvent;

public interface IEventListener extends IBaseComponent {
	
	/**
	 * 内部触发
	 * 
	 * @param event
	 * @param chain
	 * @param applicationContext
	 * @param servletContext
	 * @throws Exception
	 */
	void invoke(BaseEvent event, ListenerChain chain, ApplicationContext applicationContext, ServletContext servletContext) throws Exception;
	
	/**
	 * 回调方法
	 * 
	 * @param event
	 * @param chain
	 * @param applicationContext
	 * @param servletContext
	 * @throws Exception
	 */
	void onFire(BaseEvent event, ListenerChain chain, ApplicationContext applicationContext, ServletContext servletContext) throws Exception;
	
	/**
	 * 获取受监听事件的编号
	 * 
	 * @return
	 */
	String getListenEventName();

}
