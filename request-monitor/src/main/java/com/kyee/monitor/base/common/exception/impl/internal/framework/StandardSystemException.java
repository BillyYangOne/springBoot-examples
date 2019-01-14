package com.kyee.monitor.base.common.exception.impl.internal.framework;

import com.kyee.monitor.base.common.exception.beans.impl.SystemExceptionDesc;
import com.kyee.monitor.base.common.exception.impl.SystemException;

/**
 * 标准系统异常
 */
public class StandardSystemException extends SystemException {

	private static final long serialVersionUID = 1L;

	public StandardSystemException(SystemExceptionDesc desc) {
		super(desc);
	}
	
	@Override
	public String defShortName() {
		return "标准系统异常";
	}
}
