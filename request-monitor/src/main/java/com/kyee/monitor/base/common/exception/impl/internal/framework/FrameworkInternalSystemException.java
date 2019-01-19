package com.kyee.monitor.base.common.exception.impl.internal.framework;

import com.kyee.monitor.base.common.exception.beans.impl.SystemExceptionDesc;
import com.kyee.monitor.base.common.exception.impl.SystemException;

/**
 * 框架内部系统异常
 */
public class FrameworkInternalSystemException extends SystemException {

	private static final long serialVersionUID = 1L;

	public FrameworkInternalSystemException(SystemExceptionDesc desc) {
		super(desc);
	}
	
	@Override
	public String defShortName() {
		return "框架内部异常";
	}
}
