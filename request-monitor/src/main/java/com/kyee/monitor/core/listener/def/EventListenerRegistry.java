package com.kyee.monitor.core.listener.def;

import java.util.List;

public class EventListenerRegistry {

	private List<IEventListener> listeners;

	public List<IEventListener> getListeners() {
		return listeners;
	}

	public void setListeners(List<IEventListener> listeners) {
		this.listeners = listeners;
	}
	
}
