package com.kyee.monitor.base.common.exception;

import com.kyee.monitor.base.common.exception.beans.BaseExceptionDesc;

/**
 * 异常基类
 */
@SuppressWarnings("serial")
public abstract class BaseException extends RuntimeException {
	
	private BaseExceptionDesc desc;
	
	public BaseException(BaseExceptionDesc desc){
		this.desc = desc;
	}

	public BaseExceptionDesc getDesc() {
		return desc;
	}

	public void setDesc(BaseExceptionDesc desc) {
		this.desc = desc;
	}
	
	/**
	 * 定义简称
	 * 
	 * @return
	 */
	public abstract String defShortName();
	
	/**
	 * 定义异常类型
	 * 
	 * @return
	 */
	public abstract ExceptionType defExceptionType();
	
	@Override
	public String getMessage() {
		return desc.toJson();
	}
}
