package com.kyee.monitor;

import java.util.Properties;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.kyee.monitor.base.common.exception.beans.impl.SystemExceptionDesc;
import com.kyee.monitor.base.common.exception.impl.internal.framework.StandardSystemException;
import com.kyee.monitor.base.component.BaseComponent;
import com.kyee.monitor.core.boot.IBootService;

/**
 *@describe  监控启动器
 */
@Component
public class MonitorBootstrap extends BaseComponent implements ApplicationListener<ContextRefreshedEvent> {

    private Properties agentConfig;

    private static  final String SPRINGBOOT_ROOTCONTEX_TID = "bootstrap";

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        //只在root application context加载完成后执行
        if (event.getApplicationContext().getParent() == null
                || event.getApplicationContext().getParent().getId().contains(SPRINGBOOT_ROOTCONTEX_TID)) {

            try {

                bootStrap();

            } catch (Exception e) {

                logger.error("监控服务初始化失败!");
                logger.error("错误信息: " + e.getMessage());
            }

        }

    }

    /**
     * 启动Agent
     */
    private void bootStrap() {

        //获取监控配置
        agentConfig = getConfigurationService().getApplicationCfg();

        //检查开关是否开启
        checkAgentSwitch();

        //启动
        startup();

    }

    /**
     * @describe 检查开关是否开启
     */
    private void checkAgentSwitch() {
    	
        if (!"on".equalsIgnoreCase(agentConfig.getProperty("runningSwitch"))
                && !"true".equalsIgnoreCase(agentConfig.getProperty("runningSwitch"))) {

            throw new StandardSystemException(new SystemExceptionDesc("监控开关: runningSwitch值为off, 不执行初始化！"));
        }
    }

    /**
     * @describe 注册服务
     */
    private void startup() {

        ((IBootService) applicationContext.getBean("monitorBootService")).boot();
        logger.info("监控初始化完成!");
    }

}
