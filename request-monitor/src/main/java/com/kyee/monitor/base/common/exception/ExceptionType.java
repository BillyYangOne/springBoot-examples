package com.kyee.monitor.base.common.exception;

/**
 * 异常分类
 */
public enum ExceptionType {

	///////////////////////////////////////////
	///系统异常类
	///////////////////////////////////////////
	
	/**
	 * 系统异常
	 */
	SYSTEM_EXCEPTION,
	
	///////////////////////////////////////////
	///业务异常类
	///////////////////////////////////////////
	
	/**
	 * 业务异常
	 */
	BUSINESS_EXCEPTION,
	 
	///////////////////////////////////////////
	///协议异常类
	///////////////////////////////////////////
	
	/**
	 * 协议异常
	 */
	PROTOCOL_EXCEPTION,
	
	/**
	 * 无效协议异常
	 */
	PROTOCOL_OF_INVALID_EXCEPTION,
	
	/**
	 * 协议 rpc 调用异常
	 */
	PROTOCOL_OF_RPC_EXCEPTION,
	
	/**
	 * 可忽略的协议异常
	 */
	PROTOCOL_OF_IGNORABLE_EXCEPTION
}
