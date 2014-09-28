package com.uc;

import java.util.ArrayList;

public class Main {

	private static String[][] trim(String[] args) {
		ArrayList<String[]> storeage = new ArrayList<String[]>();
		for (int i = 0; i < args.length; i += 3) {
			String[] toPush;
			if (i + 2 < args.length) {
				toPush = new String[3];
				toPush[0] = args[i];
				toPush[1] = args[i + 1];
				toPush[2] = args[i + 2];
			} else {
				toPush = new String[args.length - i];
				for (int j = 0; j < args.length - i; ++j) {
					toPush[j] = args[i + j];
				}
			}
			storeage.add(toPush);
		}
		return storeage.toArray(new String[0][]);
	}

	public static void main(String[] __args__) {
		CCCFunction myfunc = new CCCFunction();
		if (__args__.length < 3) {
			String ret;
			ret = myfunc.callCCC(11, "3.2.18.278", 0x54213260);
			System.out.println("ret = " + ret.toString());
			return;
		}
		for (String[] args : trim(__args__)) {
			String ret;
			if (args.length != 3)
				break;
			else {
				Integer p0 = Integer.valueOf(args[0]);
				String p1 = args[1];
				Integer p2 = Integer.valueOf(args[2]);
				ret = myfunc.callCCC(p0, p1, p2);
			}
			System.out.println("ret = " + ret.toString());
		}
	}
}
