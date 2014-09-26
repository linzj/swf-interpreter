package com.uc.interpretor;

import java.util.HashMap;

import com.uc.parser.ByteCode;
import com.uc.parser.ByteCodeType;
import com.uc.parser.Code;
import com.uc.parser.QName;

class Context {
	int MAX_STACK;
	Object[] stack;
	int stack_end = 0;
	Context prev = null;
	Function function;
	public Interpretor interpretor;
	public Object[] locals;
	int pc = 0;
	private Code code;
	private HashMap<String, Integer> label_map = new HashMap<String, Integer>();

	private void setupLocals(Object receiver, Object[] params) {
		locals[0] = receiver;
		for (int i = 0; i < params.length; ++i) {
			locals[i + 1] = params[i];
		}
	}

	private void setupLabel() {
		int count = 0;
		for (ByteCode bytecode : code.byte_codes) {
			count++;
			if (bytecode.type_index == ByteCodeType.LABEL) {
				if (bytecode.operands != null && bytecode.operands.length == 1) {
					String name = (String) bytecode.operands[0];
					label_map.put(name, count);
				}
			}
		}
	}

	Context(Function f, Interpretor interpretor, Object receiver,
			Object[] params) {
		function = f;
		code = f.getData().code;
		locals = new Object[f.getData().local_count];
		MAX_STACK = f.getData().max_stack;
		stack = new Object[MAX_STACK];
		this.interpretor = interpretor;
		setupLocals(receiver, params);
		// set up label
		setupLabel();
	}

	public Object pop() {
		if (stack_end == 0)
			throw new IllegalStateException("stack end is 0");
		Object o = stack[stack_end - 1];
		stack_end -= 1;
		return o;
	}

	public void push(Object o) {
		if (stack_end == MAX_STACK)
			throw new IllegalStateException("stack end is MAX_STACK");
		stack[stack_end++] = o;
	}

	public void jumpTo(String label_name) {
		Integer offset = label_map.get(label_name);
		pc = offset;
	}

	public Object findProperty(QName qName) {
		if (interpretor.tryFindInGlobal(qName))
			return interpretor.getGlobalObject();
		return Interpretor.NULL_OBJECT;
	}

	public Object findPropertyStrict(QName qName) {
		Object o = findProperty(qName);
		if (o != Interpretor.NULL_OBJECT)
			return o;
		throw new IllegalStateException("findPropertyStrict always find object");
	}

	public Object getLex(QName qName) {
		Object o = interpretor.findInGlobal(qName);
		if (o == null)
			return Interpretor.NULL_OBJECT;
		return o;
	}

	public void returnValue() {
		interpretor.setReturn(true);
	}

	public void returnVoid() {
		interpretor.setReturn(false);
	}

	public void setProperty(Object objectToFind, QName qName, Object value) {
		Receiver kv = (Receiver) objectToFind;
		kv.set(qName, value);
	}

	public ByteCode getCurrentCode() {
		return code.byte_codes.get(pc++);
	}
}
