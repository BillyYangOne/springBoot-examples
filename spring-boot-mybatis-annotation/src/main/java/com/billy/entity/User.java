package com.billy.entity;

import com.billy.enums.UserSex;

/**
 * @describe 用户实体
 * @author BillyYang
 *
 */
public class User {

	private int id;
	private String userName;
	private String passWord;
	private UserSex userSex;
	private String nickName;
	
	public User() {
	}

	public User(String userName, String passWord, UserSex userSex, String nickName) {
		super();
		this.userName = userName;
		this.passWord = passWord;
		this.userSex = userSex;
		this.nickName = nickName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public UserSex getUserSex() {
		return userSex;
	}

	public void setUserSex(UserSex userSex) {
		this.userSex = userSex;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", userName=" + userName + ", passWord=" + passWord + ", userSex=" + userSex
				+ ", nickName=" + nickName + "]";
	}
	
}
