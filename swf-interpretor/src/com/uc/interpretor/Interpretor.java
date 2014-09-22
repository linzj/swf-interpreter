package com.uc.interpretor;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import com.uc.parser.ByteCode;
import com.uc.parser.ByteCodeType;
import com.uc.parser.Function;
import com.uc.parser.QName;

public class Interpretor {
	public static final Object NULL_OBJECT = new Object(); // just for test
	static final Object UNDEFIED_OBJECT = new Object(); // just for test
	static ByteCodeInterpretor[] byte_code_interpretors = new ByteCodeInterpretor[ByteCodeType.BYTECODE_COUNT];
	static {
		Class<?> interpretor_class = null;
		try {
			interpretor_class = Class
					.forName("com.uc.interpretor.Interpretors");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new IllegalStateException(
					"unable to init Interpretors class", e);
		}
		for (Class<?> c : interpretor_class.getDeclaredClasses()) {
			tryAddClass(c);
		}
		// validate the byte_code_interpretors
		validateByteCodeInterpretors();
	}

	private Context current_context = null;
	private boolean current_return = false;
	private AppDomain app_domain;

	public Interpretor(AppDomain app_domain) {
		this.app_domain = app_domain;
	}

	public Object callFunction(Function f, Object receiver, Object[] params) {
		Context c = new Context(f, this, receiver, params);
		c.prev = current_context;
		current_context = c;
		return interpret();
	}

	private Object interpret() {
		while (current_return == false) {
			// interpret one.
			ByteCode code = current_context.getCurrentCode();
			ByteCodeInterpretor interpretor = getInterpretor(code);
			Object shouldPush = interpretor.interpret(code, current_context);
			if(shouldPush != null)
				current_context.push(shouldPush);
		}
		Object retVal = current_context.pop();
		Context willBeDetach = current_context;
		current_context = current_context.prev;
		willBeDetach.prev = null;
		return retVal;
	}

	private ByteCodeInterpretor getInterpretor(ByteCode c) {
		return byte_code_interpretors[c.type_index];
	}

	public Object call(Object[] operands) {
		Integer should_pop = (Integer) operands[1];
		Object[] params = new Object[should_pop];
		for (int i = 0; i < should_pop; ++i) {
			params[i] = current_context.pop();
		}
		Object receiver = current_context.pop();
		Function f = findFunction((QName) operands[0]);
		if (f == null)
			throw new IllegalStateException("fails to find function: " + operands[0].toString());
		return callFunction(f, receiver , params);
	}

	private Function findFunction(QName qName) {
		// TODO Auto-generated method stub
		return null;
	}

	private static void tryAddClass(Class<?> c) {
		int modifiers = c.getModifiers();
		// only the static non abtract class will processed.
		if ((modifiers & Modifier.ABSTRACT) != 0)
			return;
		if ((modifiers & Modifier.STATIC) == 0)
			return;
		if (!ByteCodeInterpretor.class.isAssignableFrom(c))
			return;
		int[] handling_code = null;
		// only the type with code static field will processed.
		for (Field f : c.getDeclaredFields()) {
			int field_modifiers = f.getModifiers();
			if ((field_modifiers & Modifier.STATIC) == 0)
				continue;
			if ((field_modifiers & Modifier.FINAL) == 0)
				continue;
			if (f.getName().equals("code") == false)
				continue;
			try {
				Object code = f.get(f);
				if (code instanceof Integer) {
					handling_code = new int[] { (Integer) code };
					break;
				}
				if (code instanceof int[]) {
					handling_code = (int[]) code;
					break;
				}
				continue;
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// continue process
			}
		}
		if (handling_code == null) {
			// will not processed.
			return;
		}
		ByteCodeInterpretor instance = null;
		try {
			instance = (ByteCodeInterpretor) c.newInstance();
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		if (instance == null)
			return;
		for (int code : handling_code) {
			byte_code_interpretors[code] = instance;
		}
	}

	private static void validateByteCodeInterpretors() {
		int count = 0;
		for (ByteCodeInterpretor c : byte_code_interpretors) {
			if (c == null)
				throw new IllegalStateException(
						"byte_code_interpretors fails to cover all the byte codes.");
			count++;
		}
	}

	public Object loadAppDomain(Integer offset, int what) {
		return app_domain.loadAppDomain(offset,what);
	}

	public void setAppDomain(Object value, Integer offset, int what) {
		app_domain.setAppDomain(value, offset, what);
	}

	public boolean tryFindInGlobal(QName qName) {
		return app_domain.tryFindInGlobal(qName);
	}

	public Object findInGlobal(QName qName) {
		return app_domain.findInGlobal(qName);
	}

	public Object getGlobalObject() {
		return app_domain.getGlobalObject();
	}

	public void setReturn() {
		this.current_return = true;
	}

}
