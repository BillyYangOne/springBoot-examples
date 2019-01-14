package com.kyee.monitor.core.listener.def;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletContextAware;

import com.kyee.monitor.base.event.BaseEvent;
import com.kyee.monitor.base.logging.Log;
import com.kyee.monitor.base.logging.LogFactory;


/**
 * @describe 事件驱动器
 */
@Component
public class SystemEventDriver implements ApplicationListener<BaseEvent>, ApplicationContextAware, ServletContextAware {

	private Log logger = LogFactory.getLog(getClass());
	
	private ApplicationContext applicationContext;
	
	private ServletContext servletContext;
	
	@Autowired(required=false)
	@Qualifier("listenerRegistry")
	private EventListenerRegistry registry;
		
	@PostConstruct
	private void printInfo(){
		
		if(registry != null && registry.getListeners() != null){
		
			logger.info("事件监听器注册完毕，共 " + registry.getListeners().size() + " 个：" + registry.getListeners());
		}
	}
	
	@Override
	public void onApplicationEvent(BaseEvent event) {
		
		String eventCode = event.getCode().trim();
				
		List<IEventListener> listeners = filterByEventCode(eventCode);
		if(listeners.size() > 0){
			
			try {
				new ListenerChain(listeners).fire(event, applicationContext, servletContext);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 *
	 * @describe 动态注册Listeners
	 *
	 * @param listeners
	 * returnType：void
	 * </pre>
	 */
	public void addListeners(List<IEventListener> listeners) {

		if(registry != null) {

			List<IEventListener> wholeListeners = registry.getListeners();
			wholeListeners.addAll(listeners);

			registry.setListeners(wholeListeners);

		} else {

			registry = new EventListenerRegistry();
			registry.setListeners(listeners);
		}

		logger.info("事件监听器注册完毕，共 " + registry.getListeners().size() + " 个：" + registry.getListeners());
	}

	/**
	 * 销毁
	 */
	public void destroyListeners(){

		if(registry != null) {
			registry.setListeners(new ArrayList<IEventListener>());
		}
		logger.info("事件监听器销毁成功!");
	}


	/**
	 * 根据指定的 code 过滤 Listener
	 * 
	 * @param eventCode
	 * @return
	 */
	private List<IEventListener> filterByEventCode(String eventCode){
		
		List<IEventListener> resultListeners = new LinkedList<IEventListener>();
		
		if(registry != null && registry.getListeners() != null && registry.getListeners().size() > 0){
			
			for(IEventListener listener : registry.getListeners()){
				
				if(eventCode.equals(listener.getListenEventName().trim())){
					
					resultListeners.add(listener);
				}
			}
		}
		
		return resultListeners;
	}

	public EventListenerRegistry getRegistry() {
		return registry;
	}

	@Override
	public void setServletContext(ServletContext servletContext) {
		this.servletContext = servletContext;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.applicationContext = applicationContext;
	}
}
