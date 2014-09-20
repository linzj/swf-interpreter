package com.uc.parser;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;

public class Parser {
	private BufferedReader src;
	private Function context;
	private int line_count = 1;

	interface OperandParser {
		Object parse(String operand_string);
	}

	private HashMap<Integer, OperandParser> operand_parsers;

	private Parser(InputStream src) {
		this.src = new BufferedReader(new InputStreamReader(src));
		this.context = new Function();
		operand_parsers = new HashMap<Integer, OperandParser>();
		operand_parsers.put(ByteCodeType.LABEL, new OperandParser() {

			@Override
			public Object parse(String operand_string) {
				return operand_string;
			}
		});
		OperandParser if_parser = new OperandParser() {

			@Override
			public Object parse(String operand_string) {
				try {
					return Integer.valueOf(operand_string);
				} catch (NumberFormatException e) {
					return null;
				}
			}
		};
		operand_parsers.put(ByteCodeType.IFEQ, if_parser);
		operand_parsers.put(ByteCodeType.IFGE, if_parser);
		operand_parsers.put(ByteCodeType.IFGT, if_parser);
		operand_parsers.put(ByteCodeType.IFLT, if_parser);
		operand_parsers.put(ByteCodeType.IFNE, if_parser);
	}

	private String formatError(String desc) {
		return String.format("line %d: %s", this.line_count, desc);
	}

	private void parseMaxScopedDepth(String depth) throws ParseException {
		try {
			this.context.init_scoped_depth = Integer.valueOf(depth);
		} catch (NumberFormatException e) {
			throw new ParseException(formatError("parseMaxScopedDepth"), e);
		}
	}

	private void parseInitScopedDepth(String depth) throws ParseException {
		try {
			this.context.init_scoped_depth = Integer.valueOf(depth);
		} catch (NumberFormatException e) {
			throw new ParseException(formatError("parseInitScopedDepth"), e);
		}
	}

	private void parseLocalCount(String local_count) throws ParseException {
		try {
			this.context.local_count = Integer.valueOf(local_count);
		} catch (NumberFormatException e) {
			throw new ParseException(formatError("parseLocalCount"), e);
		}
	}

	private void parseMaxStack(String max_stack) throws ParseException {
		try {
			this.context.max_stack = Integer.valueOf(max_stack);
		} catch (NumberFormatException e) {
			throw new ParseException(formatError("parseMaxStack"), e);
		}
	}

	private void parseMethod() throws ParseException {
		try {
			String shouldBeMethod = this.src.readLine();
			if (shouldBeMethod == null) {
				throw new ParseException(formatError("unexpected EOF"));
			}
			if (!shouldBeMethod.equals("method")) {
				throw new ParseException(formatError("expecting method"));
			}
			this.line_count++;
			String function_name = this.src.readLine();
			if (!function_name.startsWith("name")) {
				throw new ParseException(formatError("expecing name"));
			}
			this.line_count++;
			function_name = function_name.substring(5);
			context.name = function_name;
			while (true) {
				String param = this.src.readLine();
				if (param.equals("")) {
					break;
				}
				if (!param.startsWith("param") && !param.startsWith("returns")) {
					throw new ParseException("expecting param");
				}
				this.line_count++;
				if (param.startsWith("param"))
					this.context.param_count++;
			}
			this.line_count++;
		} catch (IOException e) {
			throw new ParseException(e);
		}
	}

	private void parseBody() throws ParseException {
		try {
			String shouldBeBody = this.src.readLine();
			if (shouldBeBody == null) {
				throw new ParseException(formatError("unexpected EOF"));
			}
			if (!shouldBeBody.equals("body")) {
				throw new ParseException(formatError("expecting  body"));
			}
			this.line_count++;
			while (true) {
				String line = this.src.readLine();
				if (line == null) {
					throw new ParseException(formatError("unexpected EOF"));
				}
				if (line.equals("")) {
					break;
				}
				if (line.startsWith("maxstack")) {
					parseMaxStack(line.substring(9).trim());
				} else if (line.startsWith("localcount")) {
					parseLocalCount(line.substring(11).trim());
				} else if (line.startsWith("initscopedepth")) {
					parseInitScopedDepth(line.substring(15).trim());
				} else if (line.startsWith("maxscopedepth")) {
					parseMaxScopedDepth(line.substring(14).trim());
				} else {
					throw new ParseException(
							formatError("unexpected identifier"));
				}
				this.line_count++;
			}
			this.line_count++;
		} catch (IOException e) {
			throw new ParseException(e);
		}
	}

	private Object tryParseQName(String part) {
		if (part.startsWith("Qname")) {
			do {
				int start_of_first_quote = 23;
				int end_of_first_quote = part.indexOf('"',
						start_of_first_quote + 1);
				if (end_of_first_quote == -1)
					break;
				int start_of_second_quote = part.indexOf('"',
						end_of_first_quote + 1);
				if (start_of_second_quote == -1)
					break;
				int end_of_second_quote = part.indexOf('"',
						start_of_second_quote + 1);
				if (end_of_second_quote == -1)
					break;
				return new QName(part.substring(start_of_first_quote,
						end_of_first_quote), part.substring(
						start_of_second_quote, end_of_second_quote));
			} while (false);
		}
		return null;
	}

	private Object tryParseInteger(String part) {
		try {
			Integer i = Integer.valueOf(part);
			return i;
		} catch (NumberFormatException e) {

		}
		return null;
	}

	private void parseOperands(ByteCode code, String[] parts)
			throws ParseException {
		code.operands = new Object[parts.length - 1];
		for (int i = 1; i < parts.length; ++i) {
			Object operand = null;
			String part = parts[i];
			operand = tryParseInteger(part);
			if (operand != null) {
				code.operands[i - 1] = operand;
				continue;
			}
			operand = tryParseQName(part);
			if (operand != null) {
				code.operands[i - 1] = operand;
				continue;
			}
			OperandParser operand_parser = this.operand_parsers
					.get(code.type_index);
			if (operand_parser == null) {
				throw new ParseException(formatError("unknow operand"));
			}
			operand = operand_parser.parse(part);
			if (operand != null) {
				code.operands[i - 1] = operand;
				continue;
			}
			throw new ParseException(formatError("unknow operand"));
		}
	}

	private void parseByteCode(String[] parts) throws ParseException {
		ByteCodeType type = ByteCodeType.findByName(parts[0]);
		if (type == null) {
			throw new ParseException("unknow byte code: " + parts[0]);
		}
		ByteCode code = new ByteCode();
		code.type_index = type.index;
		parseOperands(code, parts);
		this.context.code.byte_codes.add(code);
	}

	private void parseCode() throws ParseException {
		try {
			String shouldBeCode = this.src.readLine();
			if (!shouldBeCode.equals("code")) {
				throw new ParseException(formatError("expecting method"));
			}
			this.line_count++;
			while (true) {
				String line = this.src.readLine();
				if (line == null || line.equals("")) {
					break;
				}
				String[] parts = line.split("\\s");
				parseByteCode(parts);
				this.line_count++;
			}
			this.line_count++;
		} catch (EOFException e) {
			// eat this, and finish parsing.
		} catch (IOException e) {
			throw new ParseException(e);
		}
	}

	private Function parse() throws ParseException {
		parseMethod();
		parseBody();
		parseCode();
		return this.context;
	}

	public static Function parse(InputStream input) throws ParseException {
		return new Parser(input).parse();
	}
}
