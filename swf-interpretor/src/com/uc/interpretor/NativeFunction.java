package com.uc.interpretor;

public interface NativeFunction {
	public Object call(Object receiver, Object[] params);
}
