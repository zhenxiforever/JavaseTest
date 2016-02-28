package com.simle.study.spring.core.context.support;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.simle.study.spring.core.bean.Bean;
import com.simle.study.spring.core.bean.Property;
import com.simle.study.spring.core.comm.ClassConfigParam;

public class BeanFactory {

	private List<Bean> beanList;
	
	protected Map<String,Object> alreadyDefinitionBeanByName;
	
	protected Map<Class,List<Object>> alreadyDefinitionBeanByType;
	
	public BeanFactory() {
		beanList = new ArrayList<Bean>();
		alreadyDefinitionBeanByName = new HashMap<String,Object>();
		alreadyDefinitionBeanByType = new HashMap<Class,List<Object>>();
	}

	@SuppressWarnings("unchecked")
	public void readXML(String[] location) throws DocumentException{
		for(String configLocation:location){
			SAXReader sr = new SAXReader();
			System.out.println(System.getProperty("user.dir"));
			String filename = getClass().getResource("/").getFile().toString()+configLocation;
			Document doc = sr.read(new File(filename));
			Element root = doc.getRootElement();
			List<Element> beans = root.elements(ClassConfigParam.BEAN);
			for(Element bean:beans){
				List<Attribute> attibutes = bean.attributes();
				Bean beanIns = new Bean();
				for(Attribute attr:attibutes){
					if(ClassConfigParam.BEAN_TYPE.equals(attr.getName())){
						beanIns.setClass_type(attr.getValue());
					}else{
						try {
							Method method =  beanIns.getClass().getMethod("set"+attr.getName().substring(0, 1).toUpperCase()+attr.getName().substring(1),String.class);
							method.invoke(beanIns, attr.getValue());
						} catch (SecurityException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				List<Element> properties = bean.elements(ClassConfigParam.BEAN_PROPERTIES);
				List<Property> beanPro = null;
				for(Element property:properties){
					beanPro = new ArrayList<Property>();
					List<Attribute> atts = property.attributes();
					Property pro = new Property();
					for(Attribute attr:atts){
							try {
								Method method =  pro.getClass().getMethod("set"+attr.getName().substring(0, 1).toUpperCase()+attr.getName().substring(1),String.class);
								method.invoke(pro, attr.getValue());
							} catch (SecurityException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (NoSuchMethodException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
						}
					}
					beanPro.add(pro);
				}
				beanIns.setProperties(beanPro);
				beanList.add(beanIns);
			}
		}
	}
	
	public void createBeans() throws ClassNotFoundException{
		if(beanList==null||beanList.size()<1){
			return;
		}
		for(Bean bean:beanList){
			String clazz = bean.getClass_type();
			if(!"".equals(clazz)){
				Class cla = Class.forName(clazz);
				try {
					Object obj = cla.newInstance();
					if(!"".equals(bean.getId())){
						alreadyDefinitionBeanByName.put(bean.getId(), obj);
					}
					List<Object> claTypeList = alreadyDefinitionBeanByType.get(cla);
					if(claTypeList == null){
						claTypeList = new ArrayList<Object>();
					}
					claTypeList.add(obj);
				} catch (InstantiationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void autoWarie() {
		for(Bean bean:beanList){
			 List<Property> proList = bean.getProperties();
			 if(proList==null){
				 continue;
			 }
			 Object obj = alreadyDefinitionBeanByName.get(bean.getId());
			 for(Property pro:proList){
				 String name = "set"+pro.getName().substring(0,1).toUpperCase()+pro.getName().substring(1);
				 if(pro.getValue()!=null&&!"".equals(pro.getValue())){
					try {
						Method met = obj.getClass().getMethod(name, String.class);
						met.invoke(obj, pro.getValue());
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					 
				 }else if(pro.getRef()!=null&&!"".equals(pro.getRef())){
					Object para = alreadyDefinitionBeanByName.get(pro.getRef());
					try {
						Method met = obj.getClass().getMethod(name, para.getClass());
						met.invoke(obj, para);
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (InvocationTargetException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (NoSuchMethodException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				 }
			 }
		}
	}
	
	public Object getBean(String beanName) {
		return alreadyDefinitionBeanByName.get(beanName);
	}

	public <T> Object getBean(String beanName, Class<T> classType) {
		Object obj = alreadyDefinitionBeanByName.get(beanName);
		if(obj==null){
			obj = alreadyDefinitionBeanByType.get(classType);
		}
		return obj;
	}

	public Object getBean(Class classType) {
		return alreadyDefinitionBeanByType.get(classType);
	}
}
