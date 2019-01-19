package com.billy.config;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.billy.job.WeatherDataSyncJob;

@Configuration
public class QuartzConfiguration {
	
	private final int TIME = 1800;

	@Bean
	public JobDetail weatherDataJobDetail() {

		JobDetail build = JobBuilder.newJob(WeatherDataSyncJob.class).withIdentity("weatherSyncJob").storeDurably()
				.build();
		return build;
	}

	@Bean
	public Trigger simpleJobTrigger() {

		SimpleScheduleBuilder scheduleBuilder = SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(TIME)
				.repeatForever();

		SimpleTrigger trigger = TriggerBuilder.newTrigger().forJob(weatherDataJobDetail())
				.withIdentity("weatherSynTrigger")
				.withSchedule(scheduleBuilder).build();
		
		return trigger;

	}
}
