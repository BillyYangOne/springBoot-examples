package com.billy.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.billy.entity.UserEntity;
import com.billy.mapper.master.UserMasterMapper;
import com.billy.mapper.slave.UserSlaveMapper;

@RestController
public class UserController {

	@Autowired
	private UserMasterMapper masterMapper;
	@Autowired
	private UserSlaveMapper slaveMapper;

	@RequestMapping("/getUsers")
	public List<UserEntity> getUsers() {
		List<UserEntity> users = masterMapper.getAll();
		return users;
	}

	@RequestMapping("/getUser")
	public UserEntity getUser(Long id) {
		UserEntity user = slaveMapper.getOne(id);
		return user;
	}

}
