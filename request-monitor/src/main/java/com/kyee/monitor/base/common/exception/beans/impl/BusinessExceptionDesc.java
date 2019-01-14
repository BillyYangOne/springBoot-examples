package com.kyee.monitor.base.common.exception.beans.impl;

import java.util.HashMap;
import java.util.Map;

import com.kyee.monitor.base.common.exception.beans.BaseExceptionDesc;


/**
 * 业务异常描述类
 */
public class BusinessExceptionDesc extends BaseExceptionDesc{
	
	/**
	 * 显示类型
	 */
	public enum SHOW_TYPE {
		
		/**
		 * 不显示
		 */
		NONE("none"),
		
		/**
		 * 信息提示
		 */
		INFO("info"),
		
		/**
		 * 警告提示
		 */
		WARN("warn"),
		
		/**
		 * 错误提示
		 */
		ERROR("error");
		
		private String value;
		
		private SHOW_TYPE(String value){
			this.value = value;
		}
		
		@Override
		public String toString() {
			return value;
		}
	}
	
	private SHOW_TYPE showType;
	
	public BusinessExceptionDesc(String code, SHOW_TYPE showType, String message) {
		super();
		this.setCode(code);
		this.showType = showType;
		this.setMessage(message);
	}
	
	public SHOW_TYPE getShowType() {
		return showType;
	}

	public void setShowType(SHOW_TYPE showType) {
		this.showType = showType;
	}
	
	@Override
	public Map<String, Object> getSerializedData() {
		
		Map<String, Object> data = new HashMap<String, Object>();
		data.put("showType", showType); 
		data.put("exceptionType", "BUSINESS");
		data.putAll(getBaseSerializedData());
		
		return data;
	}
}
