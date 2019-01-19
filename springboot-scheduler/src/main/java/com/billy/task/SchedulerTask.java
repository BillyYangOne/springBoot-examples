package com.billy.task;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
/**
 * @describe cron表达式定时任务 
 **/
@Component
public class SchedulerTask {

	private int count = 0 ;
	
	@Scheduled(cron="*/6 * * * * ?")
	private void process() {
		System.out.println("this sheduler task is running " + (count++));
	}
}
