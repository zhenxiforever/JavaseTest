package com.simle.study.spring.core.context;

public interface ApplicationContext {

	public Object getBean(String beanName);
	
	public <T> Object getBean(String beanName,Class<T> classType);
	
	public Object getBean(Class classType);
}
