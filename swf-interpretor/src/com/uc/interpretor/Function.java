package com.uc.interpretor;

import com.uc.parser.FunctionData;

public class Function {
	private FunctionData data;

	public Function(FunctionData data) {
		this.data = data;
	}
	
	public FunctionData getData() {
		return data;
	}
}
