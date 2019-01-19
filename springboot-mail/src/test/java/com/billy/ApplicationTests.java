package com.billy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.billy.service.MailService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {
	
	@Autowired
	private MailService mailService;
	
	String to = "xxxxx@qq.com";
	String subject = "测试邮件";
	String content = "简单邮件发送，请注意查收@！！！";

	@Test
	public void sendMail() {
		
		mailService.sendMail(to, subject, content);
	}

}

