package com.kyee.monitor.base.monitor;

import org.aspectj.lang.ProceedingJoinPoint;

import com.kyee.monitor.base.component.IBaseComponent;

/**
 * 监控执行接口
 * @author 
 *
 */
public interface IMonitor extends IBaseComponent {

	/**
	 * 执行监控
	 * @param joinpoint
	 * @return
	 * @throws Throwable
	 */
	Object doMonitor(ProceedingJoinPoint joinpoint) throws Throwable;

}
