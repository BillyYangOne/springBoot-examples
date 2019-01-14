package com.kyee.monitor.base.common.exception.beans.impl;

import java.util.HashMap;
import java.util.Map;

import com.kyee.monitor.base.common.exception.beans.BaseExceptionDesc;
import com.kyee.monitor.base.common.exception.impl.SystemException;
import com.kyee.monitor.base.common.exception.impl.internal.framework.FrameworkInternalSystemException;


/**
 * 系统异常描述类
 */
public class SystemExceptionDesc extends BaseExceptionDesc {
	
	private final String DEFAULT_MESSAGE = "系统异常";

	public SystemExceptionDesc(Throwable throwable) {
		super();
		super.setThrowable(throwable);
		this.setMessage(DEFAULT_MESSAGE);
	}
	
	public SystemExceptionDesc(Throwable throwable, String code) {
		super();
		super.setThrowable(throwable);
		this.setMessage(DEFAULT_MESSAGE);
		this.setCode(code);
	}
	
	public SystemExceptionDesc(String message) {
		super();
		this.setMessage(message);
	}
	
	public SystemExceptionDesc(String message, String code) {
		super();
		this.setMessage(message);  
		this.setCode(code);
	}
	
	public SystemExceptionDesc(String message, Throwable throwable) {
		super();
		super.setThrowable(throwable);
		this.setMessage(message);
	}
	
	public SystemExceptionDesc(String message, String code, Throwable throwable) {
		super();
		super.setThrowable(throwable);
		this.setMessage(message);
		this.setCode(code);
	}
	
	/**
	 * 从 java 原生 Exception 转换为 SystemExceptionDesc
	 * 
	 * @param e
	 * @return
	 */
	public static SystemException convertFromNativeException(Exception e) {
		
		return new FrameworkInternalSystemException(new SystemExceptionDesc(e));
	}
	
	@Override
	public Map<String, Object> getSerializedData() {
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("exceptionType", "SYSTEM");
		data.putAll(getBaseSerializedData());
		
		return data;
	}
}
