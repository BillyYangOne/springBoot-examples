package com.billy.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.billy.entity.User;
import com.billy.enums.UserSex;

public interface UserMapper {

	@Select("select * from users")
	@Results({
		@Result(property = "userSex", column = "user_sex", javaType = UserSex.class),
		@Result(property = "nickName", column = "nick_name")
	})
	List<User> getAllUser();
	
	@Select("select * from users where id = #{id}")
	@Results({
		@Result(property = "userSex", column = "user_sex", javaType = UserSex.class),
		@Result(property = "nickName", column = "nick_name")
	})
	User getUserById(int id);
	
	@Insert("insert into users(userName, passWord, user_sex, nick_name) values(#{userName}, #{passWord}, #{userSex}, #{nickName})")
	void addUser(User user);
	
	@Update("update users set userName=#{userName}, passWord=#{passWord}, user_sex=#{userSex}, nick_name=#{nickName} where id=#{id}")
	void udpateUser(User user);
	
	@Delete("delete from users where id=#{id}")
	void deleteUser(User user);
}
