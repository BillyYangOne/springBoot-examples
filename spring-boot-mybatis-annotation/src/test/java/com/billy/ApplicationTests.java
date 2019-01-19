package com.billy;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.billy.entity.User;
import com.billy.enums.UserSex;
import com.billy.mapper.UserMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ApplicationTests {
	
	@Autowired
	private UserMapper userMapper;

	@Test
	public void contextLoads() {
	}
	
	@Test
	public void addUserTest() {
		
		userMapper.addUser(new User("杨乐", "123456", UserSex.BOY, "BillyYang"));
	}
	
	@Test
	public void getUserById() {
		User user = userMapper.getUserById(29);
		System.out.println(user.toString());
	}
	
	@Test
	public void testUpdate() {
		User user = userMapper.getUserById(29);
		user.setNickName("No1");
		userMapper.udpateUser(user);
	}

}

