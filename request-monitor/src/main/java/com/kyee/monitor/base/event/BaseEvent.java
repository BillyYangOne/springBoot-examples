package com.kyee.monitor.base.event;


import org.springframework.context.ApplicationEvent;

/**
 * 基础事件对象
 */
public abstract class BaseEvent extends ApplicationEvent {
	
	private static final long serialVersionUID = 1L;
	private String code;
	
	public BaseEvent(Object source) {
		super(source);
	}

	/*
	 * 获取事件编号
	 * 
	 * @return
	 */
	public String getCode() {
		
		if(code == null){
			code = defCode();
		}
		
		return code;
	}
	
	/**
	 * 定义事件编号
	 * 
	 * @return
	 */
	public abstract String defCode();
}
