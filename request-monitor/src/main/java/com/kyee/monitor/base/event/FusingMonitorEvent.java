package com.kyee.monitor.base.event;

public class FusingMonitorEvent extends BaseEvent {
	private static final long serialVersionUID = 1L;

	public FusingMonitorEvent(Object source) {
		super(source);
	}

	@Override
	public String defCode() {
		return "_FusingMonitor";
	}
}
