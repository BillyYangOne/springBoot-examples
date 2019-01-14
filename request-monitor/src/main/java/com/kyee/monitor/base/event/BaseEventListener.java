package com.kyee.monitor.base.event;

import com.kyee.monitor.base.component.BaseComponent;
import com.kyee.monitor.base.event.internal.FusingExecutionHandler;
import com.kyee.monitor.core.listener.bean.EventListenerTask;
import com.kyee.monitor.core.listener.def.IEventListener;
import com.kyee.monitor.core.listener.def.ListenerChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.ServletContext;
import java.util.Properties;
import java.util.concurrent.*;

/**
 * 基础事件监听
 */
public abstract class BaseEventListener extends BaseComponent implements IEventListener {

	private String listen;

	private ExecutorService pool;

	@Autowired
	private FusingExecutionHandler fusingExecutionHandler;

	@PostConstruct
	private void init() {

		Properties config = getConfigurationService().getApplicationCfg();

		int value = Integer.parseInt(config.getProperty("options.monitor.thread.event.pool", "100"));

		logger.info("thread.event.pool 的值为 " + value + "（如果没有设置，则默认值为 100）");

		pool = new ThreadPoolExecutor(0, value, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(),fusingExecutionHandler);
	}

	@Override
	public void invoke(final BaseEvent event, final ListenerChain chain, final ApplicationContext applicationContext,
			final ServletContext servletContext) throws Exception{

		try {
			// 过滤无用的事件
			if (isFilterEvent(event.getSource())) {
				return;
			}

			pool.submit(new EventListenerTask<Void>() {

				@Override
				public Void execute() throws Exception {
					onFire(event, chain, applicationContext, servletContext);
					if (chain.isNext()) {
						chain.fireNext(event, applicationContext, servletContext);
					}
					return null;
				}

			});
		} catch (Exception e) {
			logger.error("BaseEventListener线程池调用异常", e);
			throw e;
		}
	}

	@Override
	public String getListenEventName() {
		return this.listen;
	}

	public void setListen(String listen) {
		this.listen = listen;
	}

	/**
	 * 是否过滤事件
	 * 
	 * @return
	 */
	protected boolean isFilterEvent(Object eventSource) {
		return false;
	}

	@Override
	public String toString() {
		return "BaseEventListener [listen=" + listen + "]";
	}

	@PreDestroy
	private void destory() {

		if (pool != null) {

			logger.info("开始关闭事件监听器线程池");
			pool.shutdownNow();
			logger.info("事件监听器线程池关闭成功");
		}
	}

}
