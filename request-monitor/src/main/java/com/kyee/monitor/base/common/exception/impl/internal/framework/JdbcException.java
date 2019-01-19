package com.kyee.monitor.base.common.exception.impl.internal.framework;

import com.kyee.monitor.base.common.exception.beans.impl.SystemExceptionDesc;
import com.kyee.monitor.base.common.exception.impl.SystemException;

/**
 * jdbc 异常
 */
public abstract class JdbcException extends SystemException {

	private static final long serialVersionUID = 1L;

	public JdbcException(SystemExceptionDesc desc) {
		super(desc);
	}
	
	@Override
	public String defShortName() {
		return "jdbc 异常";
	}
}
