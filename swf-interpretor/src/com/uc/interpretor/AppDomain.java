package com.uc.interpretor;

import java.util.HashMap;

import com.uc.parser.QName;

public class AppDomain {
	private byte[] appDomain = new byte[4096 * 1024];
	private int allocate_start;

	private Module global_module = new Module(new QName("__GLOBAL__",
			"__GLOBAL__"));

	public int getAppDomainCapacity() {
		return appDomain.length;
	}

	public Object loadAppDomain(Integer offset, int what) {
		switch (what) {
		case 8:
			return appDomain[offset];
		case 32:
			// always little endian
			int i0 = appDomain[offset] & 0xff;
			int i1 = appDomain[offset + 1] & 0xff;
			int i2 = appDomain[offset + 2] & 0xff;
			int i3 = appDomain[offset + 3] & 0xff;
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
			if (value instanceof Integer)
				appDomain[offset] = (byte) ((Integer) value & 0xff);
			else if (value instanceof Byte)
				appDomain[offset] = (byte) value;
			else
				throw new IllegalStateException(
						"setAppDomain with what 8 should pass an integer value");
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

	public int allocate(int i) {
		int old = this.allocate_start;
		this.allocate_start += i;
		if (this.allocate_start >= this.appDomain.length)
			throw new IllegalStateException("Drained app domain");
		return old;
	}

	public void writeBytes(byte[] bytes, int offset) {
		if (bytes.length + offset > this.allocate_start) {
			throw new IllegalArgumentException(
					"Should never go beyone this.allocate_start");
		}
		System.arraycopy(bytes, 0, this.appDomain, offset, bytes.length);
	}

	public int allocateFromBytes(byte[] b) {
		int offset = this.allocate(b.length);
		writeBytes(b, offset);
		return offset;
	}

	public Module getGlobalModule() {
		return global_module;
	}

	public byte[] getDomainMemory() {
		return appDomain;
	}
}
