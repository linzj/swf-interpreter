package com.uc.parser;

import java.util.HashMap;

public class ByteCodeType {
	String name;
	int index;

	private ByteCodeType(String name, int index) {
		this.name = name;
		this.index = index;
	}

	public static final int ADD = 0;
	public static final int BITAND = 1;
	public static final int BITOR = 2;
	public static final int BITXOR = 3;
	public static final int CALLPROPERTY = 4;
	public static final int CALLPROPVOID = 5;
	public static final int COERCE_S = 6;
	public static final int CONVERT_D = 7;
	public static final int CONVERT_I = 8;
	public static final int CONVERT_U = 9;
	public static final int DECREMENT = 10;
	public static final int DIVIDE = 11;
	public static final int DUP = 12;
	public static final int FINDPROPERTY = 13;
	public static final int FINDPROPSTRICT = 14;
	public static final int GETLEX = 15;
	public static final int GETLOCAL = 16;
	public static final int GETLOCAL_0 = 17;
	public static final int GETLOCAL_1 = 18;
	public static final int GETLOCAL_2 = 19;
	public static final int GETLOCAL_3 = 20;
	public static final int IFEQ = 21;
	public static final int IFGE = 22;
	public static final int IFGT = 23;
	public static final int IFLT = 24;
	public static final int IFNE = 25;
	public static final int JUMP = 26;
	public static final int LABEL = 27;
	public static final int LI32 = 28;
	public static final int LI8 = 29;
	public static final int LSHIFT = 30;
	public static final int MODULO = 31;
	public static final int MULTIPLY = 32;
	public static final int PUSHBYTE = 33;
	public static final int PUSHINT = 34;
	public static final int PUSHNAN = 35;
	public static final int PUSHNULL = 36;
	public static final int PUSHSCOPE = 37;
	public static final int PUSHSHORT = 38;
	public static final int PUSHUINT = 39;
	public static final int RETURNVALUE = 40;
	public static final int RSHIFT = 41;
	public static final int SETLOCAL = 42;
	public static final int SETPROPERTY = 43;
	public static final int SI32 = 44;
	public static final int SI8 = 45;
	public static final int SUBTRACT = 46;
	public static final int SWAP = 47;
	public static final int URSHIFT = 48;
	public static final int IFTRUE = 49;
	public static final int SF64 = 50;
	public static final int INCLOCAL_I = 51;
	public static final int DECLOCAL_I = 52;
	public static final int RETURNVOID = 53;

	public static final int BYTECODE_COUNT = 54;

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
				"si8", "subtract", "swap", "urshift", "iftrue", "sf64",
				"inclocal_i", "declocal_i", "returnvoid" };
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
