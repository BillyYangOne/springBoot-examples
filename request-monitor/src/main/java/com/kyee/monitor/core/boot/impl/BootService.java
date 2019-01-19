package com.kyee.monitor.core.boot.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.kyee.monitor.base.component.BaseComponent;
import com.kyee.monitor.core.boot.IBootService;
import com.kyee.monitor.core.listener.ListenerConfig;

/**
 * @describe 启动服务
 */
@Component("monitorBootService")
@Lazy
public class BootService extends BaseComponent implements IBootService {

    private static boolean listenerConfigured = false;
    public static boolean booted = false;

    public static boolean isListenerConfigured() {
        return listenerConfigured;
    }

    public static boolean isBooted() {
        return booted;
    }

    @Override
    public void boot() {
        logger.info("BootService 开始配置监听");
        configurationListeners();
        booted = listenerConfigured;
    }

    private void configurationListeners() {
    	
        logger.info("配置 监听器 开始");
        //注册监听器
        ((ListenerConfig)applicationContext.getBean("listenerConfig")).listenerConfiguration();
        listenerConfigured = true;
        logger.info("配置 监听器 结束");
    }

    @Override
    public void pause() {

        //注销监听器
        ((ListenerConfig)applicationContext.getBean("listenerConfig")).destroyListeners();

    }

    @Override
    public void resume() {
        boot();
    }


}
