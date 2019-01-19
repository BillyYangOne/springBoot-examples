package com.kyee.monitor.core.monitor.impl;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.kyee.monitor.base.common.exception.ExceptionType;
import com.kyee.monitor.base.monitor.BaseMonitor;
import com.kyee.monitor.core.monitor.bean.DetailMonitorResult;
import com.kyee.monitor.core.monitor.bean.MonitorTargetType;


/**
 * dao 监控器
 */
@Component("daoMonitor")
public class DaoMonitor extends BaseMonitor{
	
	@Override
    public Object doMonitor(ProceedingJoinPoint joinpoint) throws Throwable {  
    	
		MethodSignature signature = (MethodSignature) joinpoint.getSignature();
		Method method = signature.getMethod();
		long startTs = -1L;
		long spendTime = -1L;
		
		String exception = null;
		ExceptionType exceptionType = null;
		String exceptionName = null;
		Object result = null;
		Integer stepId = getStepId();
		
		try {
			
			startTs = System.currentTimeMillis();
			result = joinpoint.proceed();
			spendTime = System.currentTimeMillis() - startTs;
		} catch (Exception e) {
			
			spendTime = System.currentTimeMillis() - startTs;
			exception = getExceptionStackTrace(e);
			exceptionType = getExceptionType(e);
			exceptionName = getExceptionName(e);
			
			throw e;
		} finally {

			getDetailMonitorResult().addAll(Arrays.asList(new DetailMonitorResult(getGroupId(MonitorTargetType.DAO),
					stepId,
					MonitorTargetType.DAO,
					joinpoint.getSignature().getName(),
					method + "",
					JSON.toJSONString(joinpoint.getArgs()),
					exception != null? true:false,
					exception,
					exceptionType,
					exceptionName,
					spendTime,
					startTs)));
		}
		
        return result;
    }   
}
