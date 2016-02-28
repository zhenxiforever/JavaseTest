package com.simle.study.spring;

import org.junit.Test;

import com.simle.study.spring.app.service.UserService;
import com.simle.study.spring.core.context.ApplicationContext;
import com.simle.study.spring.core.context.support.ClassPathApplicationContext;

public class SpringTest {

	@Test
	public void springTest(){
		ApplicationContext ac = new ClassPathApplicationContext("com/simle/study/spring/app/applicationContext.xml");
		Object userService =  ac.getBean("userService");
		((UserService) userService).sayHello();
	}
}
