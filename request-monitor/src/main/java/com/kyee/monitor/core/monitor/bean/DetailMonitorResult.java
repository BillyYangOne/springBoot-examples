package com.kyee.monitor.core.monitor.bean;

import com.kyee.monitor.base.common.exception.ExceptionType;

/**
 * @describe 明细
 */
public class DetailMonitorResult {

	private String groupId;
	
	private Integer stepId;
	
	private MonitorTargetType targetType;
	
	// 方法名称等信息
	private String methodName;
	
	private String methodContent;
	
	// 如果是方法，则为方法参数，SQL则为SQL参数
	private String params;

	private boolean hasException;

	private String exception;

	private ExceptionType exceptionType;

	private String exceptionName;
	
	private Long spendTime;
	
	private Long runTime;
	
	public DetailMonitorResult() {
	}
	

	public DetailMonitorResult(String groupId, Integer stepId, MonitorTargetType targetType, String methodName,
			String methodContent, String params, boolean hasException, String exception, ExceptionType exceptionType,
			String exceptionName, Long spendTime, Long runTime) {
		super();
		this.groupId = groupId;
		this.stepId = stepId;
		this.targetType = targetType;
		this.methodName = methodName;
		this.methodContent = methodContent;
		this.params = params;
		this.hasException = hasException;
		this.exception = exception;
		this.exceptionType = exceptionType;
		this.exceptionName = exceptionName;
		this.spendTime = spendTime;
		this.runTime = runTime;
	}



	@Override
	public String toString() {
		return "DetailMonitorResult [groupId=" + groupId + ", stepId=" + stepId + ", targetType=" + targetType
				+ ", methodName=" + methodName + ", methodContent=" + methodContent + ", params=" + params
				+ ", hasException=" + hasException + ", exception=" + exception + ", exceptionType=" + exceptionType
				+ ", exceptionName=" + exceptionName + ", spendTime=" + spendTime + ", runTime=" + runTime + "]";
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

	public String getMethodContent() {
		return methodContent;
	}

	public void setMethodContent(String methodContent) {
		this.methodContent = methodContent;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public boolean isHasException() {
		return hasException;
	}

	public void setHasException(boolean hasException) {
		this.hasException = hasException;
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
	
	
	
}
