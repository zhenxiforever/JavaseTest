package com.simle.study.spring.core.bean;

public class Property {

	private String name;
	
	private String value;
	
	private String ref;

	public Property() {
		super();
	}

	public Property(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	
	
}
