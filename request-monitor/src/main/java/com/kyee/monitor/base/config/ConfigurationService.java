package com.kyee.monitor.base.config;

import java.util.Properties;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.kyee.monitor.base.common.util.CommonUtils;
import com.kyee.monitor.base.logging.Log;
import com.kyee.monitor.base.logging.LogFactory;
import com.kyee.monitor.config.constant.GlobalConstantEnum;


@Component("configurationService")
public class ConfigurationService implements IConfigurationService{

	private Log logger = LogFactory.getLog(getClass());

	private Properties applicationCfg;

	@PostConstruct
	public void initConfig() {

		applicationCfg = getConfig();
		logger.info("配置文件加载完成：" + applicationCfg);
	}

	private Properties getConfig() {

		return CommonUtils.PropertiesUtil.loadClassPathProperties(GlobalConstantEnum.MINITOR_CONFIG_FILE_NAME.getValue());
	}

	@Override
	public Properties getApplicationCfg() {
		return applicationCfg;
	}
}
