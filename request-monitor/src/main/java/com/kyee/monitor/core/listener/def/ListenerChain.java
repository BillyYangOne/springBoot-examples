package com.kyee.monitor.core.listener.def;

import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;

import com.kyee.monitor.base.event.BaseEvent;

public class ListenerChain {

	private List<IEventListener> listeners;

	private Boolean next;

	private Integer index;

	public ListenerChain(List<IEventListener> listeners) {
		this.listeners = listeners;
		this.index = 0;
	}

	public void fire(BaseEvent event, ApplicationContext applicationContext, ServletContext servletContext)
			throws Exception {

		if (listeners != null && listeners.size() > 0) {

			listeners.get(0).invoke(event, this, applicationContext, servletContext);
		}
	}

	public void fireNext(BaseEvent event, ApplicationContext applicationContext, ServletContext servletContext)
			throws Exception {

		if (listeners.size() > this.index) {

			listeners.get(this.index).invoke(event, this, applicationContext, servletContext);
		}
	}

	public void next() {
		this.next = true;
		this.index++;
	}

	public void stop() {
		this.next = false;
	}

	public int getIndex() {
		return this.index;
	}

	public IEventListener getCurrentListener() {
		return this.listeners.get(this.index);
	}

	public Boolean isNext() {
		return this.next == null ? false : this.next;
	}

	public Boolean isStop() {
		return this.next == null ? true : !this.next;
	}

}
