package com.uc.parser;

import java.util.HashMap;

class ByteCodeType {
	String name;
	int index;

	private ByteCodeType(String name, int index) {
		this.name = name;
		this.index = index;
	}

	static final int ADD = 0;
	static final int BITAND = 1;
	static final int BITOR = 2;
	static final int BITXOR = 3;
	static final int CALLPROPERTY = 4;
	static final int CALLPROPVOID = 5;
	static final int COERCE_S = 6;
	static final int CONVERT_D = 7;
	static final int CONVERT_I = 8;
	static final int CONVERT_U = 9;
	static final int DECREMENT = 10;
	static final int DIVIDE = 11;
	static final int DUP = 12;
	static final int FINDPROPERTY = 13;
	static final int FINDPROPSTRICT = 14;
	static final int GETLEX = 15;
	static final int GETLOCAL = 16;
	static final int GETLOCAL_0 = 17;
	static final int GETLOCAL_1 = 18;
	static final int GETLOCAL_2 = 19;
	static final int GETLOCAL_3 = 20;
	static final int IFEQ = 21;
	static final int IFGE = 22;
	static final int IFGT = 23;
	static final int IFLT = 24;
	static final int IFNE = 25;
	static final int JUMP = 26;
	static final int LABEL = 27;
	static final int LI32 = 28;
	static final int LI8 = 29;
	static final int LSHIFT = 30;
	static final int MODULO = 31;
	static final int MULTIPLY = 32;
	static final int PUSHBYTE = 33;
	static final int PUSHINT = 34;
	static final int PUSHNAN = 35;
	static final int PUSHNULL = 36;
	static final int PUSHSCOPE = 37;
	static final int PUSHSHORT = 38;
	static final int PUSHUINT = 39;
	static final int RETURNVALUE = 40;
	static final int RSHIFT = 41;
	static final int SETLOCAL = 42;
	static final int SETPROPERTY = 43;
	static final int SI32 = 44;
	static final int SI8 = 45;
	static final int SUBTRACT = 46;
	static final int SWAP = 47;
	static final int URSHIFT = 48;

	static private ByteCodeType[] types;
	static private HashMap<String, ByteCodeType> fast_name_map = new HashMap<String, ByteCodeType>();
	static {
		String[] byte_code_names = new String[] { "add", "bitand", "bitor",
				"bitxor", "callproperty", "callpropvoid", "coerce_s",
				"convert_d", "convert_i", "convert_u", "decrement", "divide",
				"dup", "findproperty", "findpropstrict", "getlex", "getlocal",
				"getlocal_0", "getlocal_1", "getlocal_2", "getlocal_3", "ifeq",
				"ifge", "ifgt", "iflt", "ifne", "jump", "label", "li32", "li8",
				"lshift", "modulo", "multiply", "pushbyte", "pushint",
				"pushnan", "pushnull", "pushscope", "pushshort", "pushuint",
				"returnvalue", "rshift", "setlocal", "setproperty", "si32",
				"si8", "subtract", "swap", "urshift" };
		int count = 0;
		types = new ByteCodeType[byte_code_names.length];
		for (String byte_code_name : byte_code_names) {
			ByteCodeType new_type = new ByteCodeType(byte_code_name, count);
			types[count] = new_type;
			fast_name_map.put(new_type.name, new_type);
			count++;
		}
	}
	
	static ByteCodeType findByIndex(int index) {
		return types[index];
	}

	static ByteCodeType findByName(String name) {
		return fast_name_map.get(name);
	}
}
