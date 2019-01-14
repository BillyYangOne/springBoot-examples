package com.kyee.monitor.base.common.exception.impl.internal.framework;

import com.kyee.monitor.base.common.exception.beans.impl.SystemExceptionDesc;
import com.kyee.monitor.base.common.exception.impl.SystemException;

/**
 * 无效配置异常
 */
public class InvalidConfigurationException extends SystemException {

	private static final long serialVersionUID = 1L;

	public InvalidConfigurationException(SystemExceptionDesc desc) {
		super(desc);
	}
	
	@Override
	public String defShortName() {
		return "无效配置异常";
	}
}
