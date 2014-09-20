package com.uc.parser;

public class Function {
	public int param_count;
	public int max_stack;
	public int local_count;
	public int init_scoped_depth;
	public int max_scope_depth;
	public String name;
	public Code code = new Code();
	
	@Override
	public String toString() {
		return String.format("Function: %s\n" + "param_count = %d\n"
				+ "max_stack = %d\nlocal_count = %d\n"
				+ "init_scoped_depth = %d\n"
				+ "max_scope_depth = %d\ncode:\n%s\n", name, param_count,
				max_stack, init_scoped_depth, max_scope_depth, code.toString());
	}
}
