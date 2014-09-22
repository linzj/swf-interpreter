package com.uc.parser;

public class ByteCode {
	public int type_index;
	public Object [] operands;
	public int line_number;
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
