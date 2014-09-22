package com.uc.interpretor;

import java.util.HashMap;

import com.uc.parser.QName;

public class AppDomain {
	private byte[] appDomain = new byte[2048 * 1024];
	private KeyValue global_object = new KeyValue() {
		private HashMap<QName, Object> map = new HashMap<QName, Object>();

		@Override
		public Object get(QName name) {
			return map.get(name);
		}

		@Override
		public void set(QName name, Object value) {
			map.put(name, value);
		}
	};
	private int allocate_start;

	public int getAppDomainCapacity() {
		return appDomain.length;
	}

	public Object loadAppDomain(Integer offset, int what) {
		switch (what) {
		case 8:
			return appDomain[offset];
		case 32:
			// always little endian
			byte i0 = appDomain[offset];
			byte i1 = appDomain[offset + 1];
			byte i2 = appDomain[offset + 2];
			byte i3 = appDomain[offset + 3];
			int ret = 0;
			ret |= i0;
			ret |= i1 << 8;
			ret |= i2 << 16;
			ret |= i3 << 24;
			return ret;
		}
		throw new IllegalStateException("loadAppDomain fails to serve!");
	}

	public void setAppDomain(Object value, Integer offset, int what) {
		switch (what) {
		case 8:
			appDomain[offset] = (byte) ((Integer) value & 0xff);
			return;
		case 32:
			// always little endian
			int ivalue = (Integer) value;
			byte i0 = (byte) (ivalue & 0xff);
			byte i1 = (byte) ((ivalue & 0xff00) >> 8);
			byte i2 = (byte) ((ivalue & 0xff0000) >> 16);
			byte i3 = (byte) ((ivalue & 0xff000000) >> 24);
			appDomain[offset] = i0;
			appDomain[offset + 1] = i1;
			appDomain[offset + 2] = i2;
			appDomain[offset + 3] = i3;
			return;
		}
		throw new IllegalStateException("setAppDomain fails to serve!");

	}

	public boolean tryFindInGlobal(QName qName) {
		if (global_object.get(qName) != null)
			return true;
		return false;
	}

	public Object findInGlobal(QName qName) {
		return global_object.get(qName);
	}

	public Object getGlobalObject() {
		return global_object;
	}

	public void setGlobal(QName qName, Object val) {
		global_object.set(qName, val);
	}

	public int allocate(int i) {
		int old = this.allocate_start;
		this.allocate_start += i;
		if (this.allocate_start >= this.appDomain.length)
			throw new IllegalStateException("Drained app domain");
		return old;
	}
}
