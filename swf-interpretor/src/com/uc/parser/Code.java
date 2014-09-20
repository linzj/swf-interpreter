package com.uc.parser;

import java.util.LinkedList;
import java.util.List;

public class Code {
	List<ByteCode> byte_codes = new LinkedList<ByteCode>();

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
