package com.kyee.monitor.base.common.exception.impl;

import com.kyee.monitor.base.common.exception.BaseException;
import com.kyee.monitor.base.common.exception.ExceptionType;
import com.kyee.monitor.base.common.exception.beans.impl.SystemExceptionDesc;

/**
 * 系统异常基类
 */
public abstract class SystemException extends BaseException {

	private static final long serialVersionUID = 1L;

	public SystemException(SystemExceptionDesc desc) {
		super(desc);
	}
	
	@Override
	public SystemExceptionDesc getDesc() {
		return (SystemExceptionDesc)super.getDesc();
	}
	
	@Override
	public ExceptionType defExceptionType() {
		return ExceptionType.SYSTEM_EXCEPTION;
	}
	
	@Override
	public String defShortName() {
		return "系统异常";
	}
}
