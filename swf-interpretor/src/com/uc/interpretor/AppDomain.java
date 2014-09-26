package com.uc.interpretor;

import com.uc.parser.QName;

public class AppDomain {

	private Module global_module = new Module(new QName("__GLOBAL__",
			"__GLOBAL__"));

	public boolean tryFindInGlobal(QName qName) {
		if (global_module.get(qName) != null)
			return true;
		return false;
	}

	public Object findInGlobal(QName qName) {
		return global_module.get(qName);
	}

	public Object getGlobalObject() {
		return global_module;
	}

	public void setGlobal(QName qName, Object val) {
		global_module.set(qName, val);
	}

	public DomainBuffer allocate(int i) {
		DomainBuffer buffer = new DomainBuffer(i);
		return buffer;
	}

	public DomainBuffer allocateFromBytes(byte[] b) {
		DomainBuffer buffer = new DomainBuffer(b.length);
		buffer.writeBytes(b);
		return buffer;
	}

	public Module getGlobalModule() {
		return global_module;
	}
}
