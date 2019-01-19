package com.kyee.monitor.base.common.exception.beans;

import java.util.HashMap;
import java.util.Map;

import com.kyee.monitor.base.common.util.CommonUtils.JsonUtil;


/**
 * 基础异常描述类
 */
public abstract class BaseExceptionDesc {
	
	private String code;
	
	private String message;
	
	//该参数对于系统异常来说，其设置的时机为异常抛出的时候，
	//对于非系统类异常，由 ExceptionHandler 在后期设置
	private Throwable throwable;
	
	public BaseExceptionDesc(){
	}
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	public String getMessage() {
		return message;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

	/**
	 * 获取基础序列化数据
	 * 
	 * @return
	 */
	protected Map<String, Object> getBaseSerializedData() {
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("code", code);
		data.put("message", message);
		
		return data;
	}
	
	/**
	 * 获取子异常描述类需要序列化的异常数据
	 * <br/>
	 * 常用于 json 表达
	 * 
	 * @return
	 */
	public abstract Map<String, Object> getSerializedData();
	
	/**
	 * 获取当前异常的 json 表达
	 * 
	 * @return
	 */
	public String toJson() {
		return JsonUtil.object2Json(getSerializedData());
	}
}
