package com.uc.parser;

public class ByteCode {
	int type_index;
	Object [] operands;
	@Override
	public String toString() {
		ByteCodeType type = ByteCodeType.findByIndex(type_index);
		StringBuilder sb = new StringBuilder();
		sb.append(type.name);
		sb.append(" ");
		for(Object operand : operands) {
			sb.append(operand.toString());
		}
		return sb.toString();
	}
}
