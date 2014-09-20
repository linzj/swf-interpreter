package com.uc.parser;

public class ParseException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	ParseException(Throwable e) {
		super(e);
	}
	
	public ParseException(String desc) {
		super(desc);
	}
	
	public ParseException(String desc, Throwable e) {
		super(desc,e);
	}
}
