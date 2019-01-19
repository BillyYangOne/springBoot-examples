package com.billy.job;

import java.util.List;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.billy.service.WeatherService;
import com.billy.vo.City;

/**
 * 同步天气数据任务
 * @author BillyYang
 *
 */
public class WeatherDataSyncJob extends QuartzJobBean{
	
	private static Logger logger = LoggerFactory.getLogger(WeatherDataSyncJob.class);
	@Autowired
	private WeatherService weatherService;

	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		logger.info("同步天气数据！");
		/*
		try {
			List<City> listCity = weatherService.listCity();
			for (City city : listCity) {
				String cityId = city.getCityId();
				weatherService.syncWeatherDataById(cityId);
				logger.info("同步城市：" + city.getCityName() + " 完成！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info("同步天气报错！");
		}*/
		
	}

}
