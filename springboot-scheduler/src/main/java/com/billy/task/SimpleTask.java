package com.billy.task;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// 简单定时

@Component
public class SimpleTask {

	private static final SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD HH:mm:ss");
	
	@Scheduled(fixedRate=2000)
	public void printNowTime() {
		System.out.println("Now is : " + sdf.format(new Date()));
	}
}
