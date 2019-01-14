package com.kyee.monitor.core.monitor.impl.sqlmonitor;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;

import com.kyee.monitor.base.monitor.BaseMonitor;


/**
 * 基础 SQL 监控器
 */
public abstract class BaseSqlMonitor extends BaseMonitor {
	
	/**
	 * 获取 SQL 描述
	 * 
	 * @param joinpoint
	 * @return
	 */
	protected String getSqlInfo(ProceedingJoinPoint joinpoint){
				
		return Arrays.toString(joinpoint.getArgs());
	}
}
