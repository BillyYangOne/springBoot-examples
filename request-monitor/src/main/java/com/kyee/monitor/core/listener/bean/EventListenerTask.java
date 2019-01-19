package com.kyee.monitor.core.listener.bean;

import com.kyee.monitor.base.logging.Log;
import com.kyee.monitor.base.logging.LogFactory;


/**
 * @describe 后台任务
 * 
 * @param <V>
 */
public abstract class EventListenerTask<V> implements IEventListenerTask<V> {
	
	protected Log logger = LogFactory.getLog(getClass());
	
	@Override
	public V call() throws Exception {
		
		try {
			return execute();
		} catch (Exception e) {
			logger.error("事件监听器执行时捕获到异常", e);
			throw e;
		}
	}
}
