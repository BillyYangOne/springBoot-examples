package com.kyee.monitor.base.event;


/**
 * 监控结果事件
 */
public class MonitorResultEvent extends BaseEvent {
	
	private static final long serialVersionUID = 1L;

	public MonitorResultEvent(Object source) {
		super(source);
	}
	
	@Override
	public String defCode() {
		return "_MONITOR";
	}
}
