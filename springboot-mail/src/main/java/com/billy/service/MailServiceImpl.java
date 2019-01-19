package com.billy.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class MailServiceImpl implements MailService{

	private final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${mail.fromMail.addr}")
	private String from;

	@Override
	public void sendMail(String to, String subject, String content) {
		
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(content);
		
		try {
			
			mailSender.send(message);
			logger.info("邮件发送成功！");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("邮件发送失败，失败信息：" + e);
		}
		
	}
	
	
}
