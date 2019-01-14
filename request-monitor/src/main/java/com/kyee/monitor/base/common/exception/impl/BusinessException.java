package com.kyee.monitor.base.common.exception.impl;

import com.kyee.monitor.base.common.exception.BaseException;
import com.kyee.monitor.base.common.exception.ExceptionType;
import com.kyee.monitor.base.common.exception.beans.impl.BusinessExceptionDesc;

/**
 * 业务异常基类
 */
public abstract class BusinessException extends BaseException {
	
	private static final long serialVersionUID = 1L;

	public BusinessException(BusinessExceptionDesc desc) {
		super(desc);
	}
	
	@Override
	public BusinessExceptionDesc getDesc() {
		return (BusinessExceptionDesc)super.getDesc();
	}
	
	@Override
	public ExceptionType defExceptionType() {
		return ExceptionType.BUSINESS_EXCEPTION;
	}
	
	@Override
	public String defShortName() {
		return "业务异常";
	}
}
