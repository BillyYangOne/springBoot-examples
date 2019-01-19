package com.kyee.monitor.core.monitor.bean.impl;

import com.kyee.monitor.core.monitor.bean.BaseMonitorResult;
import com.kyee.monitor.core.monitor.bean.MonitorTargetType;
import com.kyee.monitor.base.common.exception.ExceptionType;

public class ControllerMonitorResult extends BaseMonitorResult {

	private String ip;
	
	private Integer port;
	
	private String exceptionMsg;
	
	private String resultCode;

	private String response;

	private String sessionParams;

	private String remoteAddr;

	public ControllerMonitorResult() {
	}

	public ControllerMonitorResult(String groupId, Integer stepId, String targetName, MonitorTargetType targetType, String methodName,
								   String requestParams, Long spendTime, Long runTime, String sourceUri, String ip, Integer port) {
		super(groupId, stepId, targetName, targetType, methodName, requestParams, spendTime, runTime, sourceUri);
		this.ip = ip;
		this.port = port;
	}

	public ControllerMonitorResult(String groupId, Integer stepId, String targetName, MonitorTargetType targetType, String methodName,
								   String requestParams, String exception, ExceptionType exceptionType, String exceptionName, String exceptionMsg, String resultCode, Long spendTime, Long runTime, String sourceUri, String ip, Integer port) {
		super(groupId, stepId, targetName, targetType, methodName, requestParams, exception, exceptionType, exceptionName, spendTime, runTime, sourceUri);
		this.ip = ip;
		this.port = port;
		this.exceptionMsg = exceptionMsg;
		this.resultCode = resultCode;
	}


	public ControllerMonitorResult(String groupId, Integer stepId, String targetName, MonitorTargetType targetType, String methodName,
								   String requestParams, String exception, ExceptionType exceptionType, String exceptionName, String exceptionMsg, String resultCode, Long spendTime, Long runTime, String sourceUri, String ip, Integer port, String response) {
		super(groupId, stepId, targetName, targetType, methodName, requestParams, exception, exceptionType, exceptionName, spendTime, runTime, sourceUri);
		this.ip = ip;
		this.port = port;
		this.exceptionMsg = exceptionMsg;
		this.resultCode = resultCode;
		this.response = response;
	}

	public ControllerMonitorResult(String groupId, Integer stepId, String targetName, MonitorTargetType targetType, String methodName,
								   String requestParams, String exception, ExceptionType exceptionType, String exceptionName, String exceptionMsg, String resultCode, Long spendTime, Long runTime, String sourceUri, String ip, Integer port, String response, String sessionParams, String remoteAddr) {
		super(groupId, stepId, targetName, targetType, methodName, requestParams, exception, exceptionType, exceptionName, spendTime, runTime, sourceUri);
		this.ip = ip;
		this.port = port;
		this.exceptionMsg = exceptionMsg;
		this.resultCode = resultCode;
		this.response = response;
		this.sessionParams = sessionParams;
		this.remoteAddr = remoteAddr;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}		
	
	public String getExceptionMsg() {
		return exceptionMsg;
	}

	public void setExceptionMsg(String exceptionMsg) {
		this.exceptionMsg = exceptionMsg;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public String getSessionParams() {
		return sessionParams;
	}

	public void setSessionParams(String sessionParams) {
		this.sessionParams = sessionParams;
	}

	public String getRemoteAddr() {
		return remoteAddr;
	}

	public void setRemoteAddr(String remoteAddr) {
		this.remoteAddr = remoteAddr;
	}

	@Override
	public String toString() {
		return "ControllerMonitorResult [groupId=" + getGroupId() + ", stepId=" + getStepId() + ", targetName=" + getTargetName() + ", targetType=" + getTargetType()
				+ ", methodName=" + getMethodName() + ", requestParams=" + getRequestParams() + ", exception=" + getException() + ", exceptionType=" + getExceptionType() + ", exceptionName=" + getExceptionName() 
				+ ", exceptionMsg=" + getExceptionMsg() + ", resultCode=" + getResultCode() 
				+ ", spendTime=" + getSpendTime() + ", runTime=" + getRunTime() + ", sourceUri=" + getSourceUri() + ", ip=" + ip + ", port=" + port + "]";
	}
	
	
}
