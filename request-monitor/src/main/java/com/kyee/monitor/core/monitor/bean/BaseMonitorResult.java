package com.kyee.monitor.core.monitor.bean;

import com.kyee.monitor.base.common.exception.ExceptionType;

/**
 * 基础监控结果
 */
public abstract class BaseMonitorResult{
	
	private String groupId;
	
	private Integer stepId;
	
	private String targetName;
	
	private MonitorTargetType targetType;
	
	private String exceptionName;
	
	private String methodName;
	
	private String requestParams;
	
	private String exception;
	
	private ExceptionType exceptionType;
	
	private Long spendTime;
	
	private Long runTime;
	
	private String sourceUri;
	
	private boolean hasException;
	
	public BaseMonitorResult() {
	}

	public BaseMonitorResult(String groupId, Integer stepId, String targetName, MonitorTargetType targetType, String methodName,
			String requestParams, Long spendTime, Long runTime, String sourceUri) {
		this.groupId = groupId;
		this.stepId = stepId;
		this.targetName = targetName;
		this.targetType = targetType;
		this.methodName = methodName;
		this.requestParams = requestParams;
		this.spendTime = spendTime;
		this.runTime = runTime;
		this.sourceUri = sourceUri;
	}
	
	public BaseMonitorResult(String groupId, Integer stepId, String targetName, MonitorTargetType targetType, String methodName,
			String requestParams, String exception, ExceptionType exceptionType, String exceptionName, Long spendTime, Long runTime, String sourceUri) {
		this.groupId = groupId;
		this.stepId = stepId;
		this.targetName = targetName;
		this.targetType = targetType;
		this.methodName = methodName;
		this.requestParams = requestParams;
		this.exception = exception;
		this.exceptionType = exceptionType;
		this.exceptionName = exceptionName;
		this.spendTime = spendTime;
		this.runTime = runTime;
		this.sourceUri = sourceUri;
	}
	
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public Integer getStepId() {
		return stepId;
	}

	public void setStepId(Integer stepId) {
		this.stepId = stepId;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	public MonitorTargetType getTargetType() {
		return targetType;
	}

	public void setTargetType(MonitorTargetType targetType) {
		this.targetType = targetType;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	
	public String getRequestParams() {
		return requestParams;
	}

	public void setRequestParams(String requestParams) {
		this.requestParams = requestParams;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public ExceptionType getExceptionType() {
		return exceptionType;
	}

	public void setExceptionType(ExceptionType exceptionType) {
		this.exceptionType = exceptionType;
	}

	public String getExceptionName() {
		return exceptionName;
	}

	public void setExceptionName(String exceptionName) {
		this.exceptionName = exceptionName;
	}

	public Long getSpendTime() {
		return spendTime;
	}

	public void setSpendTime(Long spendTime) {
		this.spendTime = spendTime;
	}

	public Long getRunTime() {
		return runTime;
	}

	public void setRunTime(Long runTime) {
		this.runTime = runTime;
	}
	
	public String getSourceUri() {
		return sourceUri;
	}

	public void setSourceUri(String sourceUri) {
		this.sourceUri = sourceUri;
	}
	
	public boolean isHasException() {
		return hasException;
	}

	public void setHasException(boolean hasException) {
		this.hasException = hasException;
	}

	@Override
	public String toString() {
		return "BaseMonitorResult [groupId=" + groupId + ", stepId=" + stepId + ", targetName=" + targetName
				+ ", targetType=" + targetType + ", methodName=" + methodName + ", requestParams=" + requestParams
				+ ", exception=" + exception + ", exceptionType=" + exceptionType + ", exceptionName=" + exceptionName + ", spendTime=" + spendTime
				+ ", runTime=" + runTime + ", sourceUri=" + sourceUri + ", hasException=" + hasException + "]";
	}
}
