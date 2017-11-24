package com.cfets.ts.s.task;

public class HashUtil {
	
	public static int lowerHashCode(String text) {
		if (text == null) {
			return 0;
		}
		// return text.toLowerCase().hashCode();
		int h = 0;
		for (int i = 0; i < text.length(); ++i) {
			char ch = text.charAt(i);
			if (ch >= 'A' && ch <= 'Z') {
				ch = (char) (ch + 32);
			}
			h = 31 * h + ch;
		}
		return h;
	}


}
