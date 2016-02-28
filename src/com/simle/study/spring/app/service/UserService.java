package com.simle.study.spring.app.service;

import com.simle.study.spring.app.dao.UserDao;

public class UserService {
	
	public UserDao userdao;
	
	public void setUserdao(UserDao userdao) {
		this.userdao = userdao;
	}

	public void sayHello(){
		userdao.sayHello();
	}
}
