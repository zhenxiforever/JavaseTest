package com.simle.study.spring.core.context.support;

import org.dom4j.DocumentException;

import com.simle.study.spring.core.context.ApplicationContext;

public class ClassPathApplicationContext extends BeanFactory implements ApplicationContext {

	private String[] configLocations;
	
	public ClassPathApplicationContext() {
		super();
	}
	
	public ClassPathApplicationContext(String configlocation) {
		this(new String[]{configlocation});
	}

	public ClassPathApplicationContext(String[] configlocation){
		super();
		this.setConfigLocations(configlocation);
		loadConfig();
	}
	
	private void loadConfig() {
		// TODO Auto-generated method stub
		try {
			readXML(configLocations);
		} catch (DocumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			createBeans();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		autoWarie();
		
	}

	public void setConfigLocations(String[] configLocations) {
		this.configLocations = configLocations;
	}

	
	public String[] getConfigLocations() {
		return configLocations;
	}
	

}
