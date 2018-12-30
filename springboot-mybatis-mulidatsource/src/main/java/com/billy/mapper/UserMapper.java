package com.billy.mapper;

import java.util.List;

import com.billy.entity.UserEntity;

public interface UserMapper {

	List<UserEntity> getAll();
	
	UserEntity getOne(Long id);

	void insert(UserEntity user);

	void update(UserEntity user);

	void delete(Long id);
}
