package com.uc.interpretor;

import java.util.HashMap;

import com.uc.parser.QName;

public class Module implements Receiver {
	private QName name;
	private HashMap<QName, Object> module_data = new HashMap<QName, Object>();

	public Module(QName name) {
		this.name = name;
	}

	@Override
	public Object get(QName name) {
		return module_data.get(name);
	}

	@Override
	public void set(QName name, Object value) {
		module_data.put(name, value);
	}
	
	public QName getName() {
		return name;
	}
}
