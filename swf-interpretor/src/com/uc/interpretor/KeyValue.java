package com.uc.interpretor;

import com.uc.parser.QName;

public interface KeyValue {
	public Object get(QName name);
	public void set(QName name, Object value);
}
