package com.uc.interpretor;

import com.uc.parser.QName;

interface Receiver {
	public Object get(QName name);
	public void set(QName name, Object value);
}
