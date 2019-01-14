package com.kyee.monitor.core.listener.bean;

import java.util.concurrent.Callable;

/**
 * @describe 后台任务接口
 * 
 * @param <V>
 */
public interface IEventListenerTask<V> extends Callable<V> {
		
	/**
	 * 执行任务
	 * 
	 * @return
	 * @throws Exception
	 */
	V execute() throws Exception;
}
