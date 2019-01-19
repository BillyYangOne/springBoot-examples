package com.kyee.monitor.base.common.exception.impl.internal.business;

import com.kyee.monitor.base.common.exception.beans.impl.BusinessExceptionDesc;
import com.kyee.monitor.base.common.exception.impl.BusinessException;

/**
 * 标准业务异常
 */
public class StandardBusinessException extends BusinessException {
	
	private static final long serialVersionUID = 1L;

	public StandardBusinessException(BusinessExceptionDesc desc) {
		super(desc);
	}
	
	@Override
	public String defShortName() {
		return "标准业务异常";
	}
}
