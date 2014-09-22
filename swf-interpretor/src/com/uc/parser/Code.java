package com.uc.parser;

import java.util.ArrayList;

public class Code {
	public ArrayList<ByteCode> byte_codes = new ArrayList<ByteCode>();

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (ByteCode byte_code : byte_codes) {
			sb.append(byte_code.toString());
			sb.append("\n");
		}
		return sb.toString();
	}
}
