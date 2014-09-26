package com.uc.interpretor;

import java.nio.ByteBuffer;

import com.uc.parser.ByteCode;
import com.uc.parser.ByteCodeType;
import com.uc.parser.QName;

class Interpretors {
	static class Add implements ByteCodeInterpretor {
		static final int code = ByteCodeType.ADD;

		@Override
		public Object interpret(ByteCode code, Context context) {
			Integer i1 = (Integer) context.pop();
			Object o2 = context.pop();
			if (o2 instanceof Integer) {
				Integer i2 = (Integer) o2;
				return i2 + i1;
			} else if (o2 instanceof DomainBuffer) {
				DomainBuffer offset = (DomainBuffer) o2;
				return offset.add(i1);
			}
			throw new IllegalStateException(
					"o2 should either DomainBuffer or Integer");
		}
	}

	static class BitAnd implements ByteCodeInterpretor {
		static final int code = ByteCodeType.BITAND;

		@Override
		public Object interpret(ByteCode code, Context context) {
			Object o1 = context.pop();
			Object o2 = context.pop();
			if (o2 instanceof Integer || o2 instanceof Byte) {
				int[] out = objectsToIntegers(o1, o2);
				int i1 = out[0];
				int i2 = out[1];

				return i2 & i1;
			} else if (o2 instanceof DomainBuffer) {
				return ((DomainBuffer) o2).and((Integer) o1);
			}
			throw new IllegalStateException(
					"o2 should either DomainBuffer or Integer");
		}
	}

	static class BitOr implements ByteCodeInterpretor {
		static final int code = ByteCodeType.BITOR;

		@Override
		public Object interpret(ByteCode code, Context context) {
			Integer i1 = (Integer) context.pop();
			Integer i2 = (Integer) context.pop();
			return i2 | i1;
		}
	}

	static class BitXOr implements ByteCodeInterpretor {
		static final int code = ByteCodeType.BITXOR;

		@Override
		public Object interpret(ByteCode code, Context context) {
			Object o1 = context.pop();
			Object o2 = context.pop();
			int[] out = objectsToIntegers(o1, o2);
			return out[1] ^ out[0];
		}
	}

	static class CallProperty implements ByteCodeInterpretor {
		static final int code = ByteCodeType.CALLPROPERTY;

		@Override
		public Object interpret(ByteCode code, Context context) {
			Interpretor interpretor = context.interpretor;
			Object ret = interpretor.call(code.operands);
			return ret;
		}
	}

	static class CallPropVoid implements ByteCodeInterpretor {
		static final int code = ByteCodeType.CALLPROPVOID;

		@Override
		public Object interpret(ByteCode code, Context context) {
			Interpretor interpretor = context.interpretor;
			interpretor.call(code.operands);
			return null;
		}
	}

	static class Coerce_s implements ByteCodeInterpretor {
		static final int code = ByteCodeType.COERCE_S;

		@Override
		public Object interpret(ByteCode code, Context context) {
			return context.pop().toString();
		}
	}

	static class Convert_d implements ByteCodeInterpretor {
		static final int code = ByteCodeType.CONVERT_D;

		@Override
		public Object interpret(ByteCode code, Context context) {
			Object o = context.pop();
			if (o instanceof String)
				return Double.valueOf(o.toString());
			else if (o instanceof Integer)
				return (double) (int) (o);
			else if (o instanceof Double)
				return o;
			else
				throw new IllegalStateException("unable to convert to double "
						+ o.toString());
		}
	}

	static class ConvertInteger implements ByteCodeInterpretor {
		static final int[] code = new int[] { ByteCodeType.CONVERT_I,
				ByteCodeType.CONVERT_U };

		@Override
		public Object interpret(ByteCode code, Context context) {
			Object o = context.pop();
			if (o instanceof String)
				return Integer.valueOf(o.toString());
			else if (o instanceof Double)
				return (int) (double) (o);
			else if (o instanceof Integer)
				return o;
			else if (o instanceof DomainBuffer)
				return o;
			else
				throw new IllegalStateException("unable to convert to integer"
						+ o.toString());
		}
	}

	static class Decrement implements ByteCodeInterpretor {
		static final int code = ByteCodeType.DECREMENT;

		@Override
		public Object interpret(ByteCode code, Context context) {
			Object o = context.pop();
			if (o instanceof Integer) {
				Integer i = (Integer) o;
				return i - 1;
			} else if (o instanceof Double) {
				Double i = (Double) o;
				return i - 1;
			} else
				throw new IllegalStateException(
						"unable to decrement non number");
		}
	}

	static class Divide implements ByteCodeInterpretor {
		static final int code = ByteCodeType.DIVIDE;

		@Override
		public Object interpret(ByteCode code, Context context) {
			Object o1 = context.pop();
			Object o2 = context.pop();
			if (o1 instanceof Double || o2 instanceof Double) {
				Double d1 = (Double) o1;
				Double d2 = (Double) o2;
				return d2 / d1;
			} else if (o1 instanceof Integer || o2 instanceof Integer) {
				Integer i1 = (Integer) o1;
				Integer i2 = (Integer) o2;
				return i2 / i1;
			} else
				throw new IllegalStateException("unable to divide non number");
		}
	}

	static class Dup implements ByteCodeInterpretor {
		static final int code = ByteCodeType.DUP;

		@Override
		public Object interpret(ByteCode code, Context context) {
			return context.stack[context.stack_end - 1];
		}
	}

	static class FindProperty implements ByteCodeInterpretor {
		static final int code = ByteCodeType.FINDPROPERTY;

		@Override
		public Object interpret(ByteCode code, Context context) {
			return context.findProperty((QName) code.operands[0]);
		}
	}

	static class FindPropStrict implements ByteCodeInterpretor {
		static final int code = ByteCodeType.FINDPROPSTRICT;

		@Override
		public Object interpret(ByteCode code, Context context) {
			return context.findPropertyStrict((QName) code.operands[0]);
		}
	}

	static class GetLex implements ByteCodeInterpretor {
		static final int code = ByteCodeType.GETLEX;

		@Override
		public Object interpret(ByteCode code, Context context) {
			return context.getLex((QName) code.operands[0]);
		}
	}

	static class Getlocal implements ByteCodeInterpretor {
		static final int code = ByteCodeType.GETLOCAL;

		@Override
		public Object interpret(ByteCode code, Context context) {
			return context.locals[(Integer) code.operands[0]];
		}
	}

	static class Getlocal_0 implements ByteCodeInterpretor {
		static final int code = ByteCodeType.GETLOCAL_0;

		@Override
		public Object interpret(ByteCode code, Context context) {
			return context.locals[0];
		}
	}

	static class Getlocal_1 implements ByteCodeInterpretor {
		static final int code = ByteCodeType.GETLOCAL_1;

		@Override
		public Object interpret(ByteCode code, Context context) {
			return context.locals[1];
		}
	}

	static class Getlocal_2 implements ByteCodeInterpretor {
		static final int code = ByteCodeType.GETLOCAL_2;

		@Override
		public Object interpret(ByteCode code, Context context) {
			return context.locals[2];
		}
	}

	static class Getlocal_3 implements ByteCodeInterpretor {
		static final int code = ByteCodeType.GETLOCAL_3;

		@Override
		public Object interpret(ByteCode code, Context context) {
			return context.locals[3];
		}
	}

	static abstract class IfFamily implements ByteCodeInterpretor {
		@Override
		public Object interpret(ByteCode code, Context context) {
			String label_name = (String) code.operands[0];
			if (success(context))
				context.jumpTo(label_name);
			return null;
		}

		protected abstract boolean success(Context context);
	}

	static class IfEq extends IfFamily {
		static final int code = ByteCodeType.IFEQ;

		@Override
		protected boolean success(Context context) {
			Object o1 = context.pop();
			Object o2 = context.pop();
			return o2.equals(o1);
		}
	}

	static class IfGE extends IfFamily {
		static final int code = ByteCodeType.IFGE;

		@Override
		protected boolean success(Context context) {
			Integer o1 = (Integer) context.pop();
			Integer o2 = (Integer) context.pop();
			return o2 >= o1;
		}
	}

	static class IfGT extends IfFamily {
		static final int code = ByteCodeType.IFGT;

		@Override
		protected boolean success(Context context) {
			Integer o1 = (Integer) context.pop();
			Integer o2 = (Integer) context.pop();
			return o2 > o1;
		}
	}

	static class IfLT extends IfFamily {
		static final int code = ByteCodeType.IFLT;

		@Override
		protected boolean success(Context context) {
			Integer o1 = (Integer) context.pop();
			Integer o2 = (Integer) context.pop();
			return o2 < o1;
		}
	}

	static class IfNE extends IfFamily {
		static final int code = ByteCodeType.IFNE;

		@Override
		protected boolean success(Context context) {
			Object o1 = context.pop();
			Object o2 = context.pop();
			int[] out = objectsToIntegers(o1, o2);
			return out[1] != out[0];
		}
	}

	static class IfTrue extends IfFamily {
		static final int code = ByteCodeType.IFTRUE;

		@Override
		protected boolean success(Context context) {
			Integer o1 = (Integer) context.pop();
			return o1 != 0;
		}
	}

	static class Jump extends IfFamily {
		static final int code = ByteCodeType.JUMP;

		@Override
		protected boolean success(Context context) {
			return true;
		}
	}

	static class Label implements ByteCodeInterpretor {
		static final int code = ByteCodeType.LABEL;

		@Override
		public Object interpret(ByteCode code, Context context) {
			return null;
		}
	}

	static class Li32 implements ByteCodeInterpretor {
		static final int code = ByteCodeType.LI32;

		@Override
		public Object interpret(ByteCode code, Context context) {
			DomainBuffer offset = (DomainBuffer) context.pop();
			return offset.loadAppDomain(32);
		}
	}

	static class Li8 implements ByteCodeInterpretor {
		static final int code = ByteCodeType.LI8;

		@Override
		public Object interpret(ByteCode code, Context context) {
			DomainBuffer offset = (DomainBuffer) context.pop();
			return offset.loadAppDomain(8);
		}
	}

	static int[] objectsToIntegers(Object o1, Object o2) {
		int[] output = new int[2];
		if (o2 instanceof Integer) {
			int i1 = (int) o1;
			int i2 = (int) o2;
			output[0] = i1;
			output[1] = i2;
			return output;
		} else if (o2 instanceof Byte) {
			int i1 = 0;
			if (o1 instanceof Byte) {
				i1 = ((int) (byte) o1) & 0xff;
			} else if (o1 instanceof Integer) {
				i1 = (int) o1;
			} else
				throw new IllegalStateException(
						"the stack should conains integers");
			int i2 = ((int) ((byte) o2) & 0xff);
			output[0] = i1;
			output[1] = i2;
			return output;
		}
		throw new IllegalStateException("the stack should conains integers");
	}

	static class LShift implements ByteCodeInterpretor {
		static final int code = ByteCodeType.LSHIFT;

		@Override
		public Object interpret(ByteCode code, Context context) {
			Object o1 = context.pop();
			Object o2 = context.pop();
			int[] out = objectsToIntegers(o1, o2);
			return out[1] << out[0];
		}
	}

	static class Modulo implements ByteCodeInterpretor {
		static final int code = ByteCodeType.MODULO;

		@Override
		public Object interpret(ByteCode code, Context context) {
			Integer i1 = (Integer) context.pop();
			Integer i2 = (Integer) context.pop();
			return i2 % i1;
		}
	}

	static class Multiply implements ByteCodeInterpretor {
		static final int code = ByteCodeType.MULTIPLY;

		@Override
		public Object interpret(ByteCode code, Context context) {
			Integer i1 = (Integer) context.pop();
			Integer i2 = (Integer) context.pop();
			return i2 * i1;
		}
	}

	static class PushInteger implements ByteCodeInterpretor {
		static final int[] code = new int[] { ByteCodeType.PUSHBYTE,
				ByteCodeType.PUSHINT, ByteCodeType.PUSHUINT,
				ByteCodeType.PUSHSHORT };

		@Override
		public Object interpret(ByteCode code, Context context) {
			return code.operands[0];
		}
	}

	static class PushNan implements ByteCodeInterpretor {
		static final int code = ByteCodeType.PUSHNAN;

		@Override
		public Object interpret(ByteCode code, Context context) {
			return Double.NaN;
		}
	}

	static class PushNull implements ByteCodeInterpretor {
		static final int code = ByteCodeType.PUSHNULL;

		@Override
		public Object interpret(ByteCode code, Context context) {
			return Interpretor.NULL_OBJECT;
		}
	}

	static class PushScope implements ByteCodeInterpretor {
		static final int code = ByteCodeType.PUSHSCOPE;

		@Override
		public Object interpret(ByteCode code, Context context) {
			return null;
		}
	}

	static class ReturnValue implements ByteCodeInterpretor {
		static final int code = ByteCodeType.RETURNVALUE;

		@Override
		public Object interpret(ByteCode code, Context context) {
			context.returnValue();
			return null;
		}
	}

	static class ReturnVoid implements ByteCodeInterpretor {
		static final int code = ByteCodeType.RETURNVOID;

		@Override
		public Object interpret(ByteCode code, Context context) {
			context.returnVoid();
			return null;
		}
	}

	static class Rshift implements ByteCodeInterpretor {
		static final int code = ByteCodeType.RSHIFT;

		@Override
		public Object interpret(ByteCode code, Context context) {
			Integer i1 = (Integer) context.pop();
			Integer i2 = (Integer) context.pop();
			return i2 >> i1;
		}
	}

	static class SetLocal implements ByteCodeInterpretor {
		static final int code = ByteCodeType.SETLOCAL;

		@Override
		public Object interpret(ByteCode code, Context context) {
			context.locals[(Integer) code.operands[0]] = context.pop();
			return null;
		}
	}

	static class SetProperty implements ByteCodeInterpretor {
		static final int code = ByteCodeType.SETPROPERTY;

		@Override
		public Object interpret(ByteCode code, Context context) {
			Object value = context.pop();
			Object objectToFind = context.pop();
			context.setProperty(objectToFind, (QName) code.operands[0], value);
			return null;
		}
	}

	static class Sf64 implements ByteCodeInterpretor {
		static final int code = ByteCodeType.SF64;

		@Override
		public Object interpret(ByteCode code, Context context) {
			DomainBuffer offset = (DomainBuffer) context.pop();
			Object value = context.pop();
			double dvalue = (double) (int) value;
			byte[] bytes = new byte[8];
			ByteBuffer.wrap(bytes).putDouble(dvalue);
			offset.writeBytes(bytes);
			return null;
		}
	}

	static class IncLocal_i implements ByteCodeInterpretor {
		static final int code = ByteCodeType.INCLOCAL_I;

		@Override
		public Object interpret(ByteCode code, Context context) {
			int who = (Integer) code.operands[0];
			int num = (Integer) context.locals[who];
			context.locals[who] = num + 1;

			return null;
		}
	}

	static class DecLocal_i implements ByteCodeInterpretor {
		static final int code = ByteCodeType.DECLOCAL_I;

		@Override
		public Object interpret(ByteCode code, Context context) {
			int who = (Integer) code.operands[0];
			int num = (Integer) context.locals[who];
			context.locals[who] = num - 1;

			return null;
		}
	}

	static class Si32 implements ByteCodeInterpretor {
		static final int code = ByteCodeType.SI32;

		@Override
		public Object interpret(ByteCode code, Context context) {
			DomainBuffer offset = (DomainBuffer) context.pop();
			Object value = context.pop();
			offset.setAppDomain(value, 32);
			return null;
		}
	}

	static class Si8 implements ByteCodeInterpretor {
		static final int code = ByteCodeType.SI8;

		@Override
		public Object interpret(ByteCode code, Context context) {
			DomainBuffer offset = (DomainBuffer) context.pop();
			Object value = context.pop();
			offset.setAppDomain(value, 8);
			return null;
		}
	}

	static class Subtract implements ByteCodeInterpretor {
		static final int code = ByteCodeType.SUBTRACT;

		@Override
		public Object interpret(ByteCode code, Context context) {
			Integer i1 = (Integer) context.pop();
			Object o2 = context.pop();
			if (o2 instanceof Integer) {
				Integer i2 = (Integer) o2;
				return i2 - i1;
			} else if (o2 instanceof DomainBuffer) {
				DomainBuffer offset = (DomainBuffer) o2;
				return offset.sub(i1);
			}
			throw new IllegalStateException(
					"o2 should either DomainBuffer or Integer");
		}
	}

	static class Swap implements ByteCodeInterpretor {
		static final int code = ByteCodeType.SWAP;

		@Override
		public Object interpret(ByteCode code, Context context) {
			Object o1 = context.pop();
			Object o2 = context.pop();
			context.push(o1);
			context.push(o2);
			return null;
		}
	}

	static class Urshift implements ByteCodeInterpretor {
		static final int code = ByteCodeType.URSHIFT;

		@Override
		public Object interpret(ByteCode code, Context context) {
			Object o1 = context.pop();
			Object o2 = context.pop();
			int[] out = objectsToIntegers(o1, o2);
			int i1 = out[0];
			int i2 = out[1];
			int ret = i2 >> i1;
			int andValue = 0;
			for (int i = 0; i < i1; ++i)
				andValue |= 1 << (31 - i);
			return ret & (~andValue);
		}
	}

}
