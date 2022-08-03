package com.ay.common.util;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	public static boolean isNull(String... params) {
		if (params == null) {
			return true;
		}
		for (String param : params) {
			if (isNull(param)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isNull(String msg) {
		return msg == null || "".equals(msg.trim()) || "null".equals(msg);
	}

	public static boolean isNumeric(String msg) {
		if (isNull(msg)) {
			return false;
		}
		int length = msg.length();
		for (int i = 0; i < length; i++) {
			if (!Character.isDigit(msg.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	public static String getNumeric(String s) {
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(s);
		return m.replaceAll("").trim();
	}

	public static boolean isMoney(String s) {
		try {
			BigDecimal money = new BigDecimal(s);
			if (money.compareTo(new BigDecimal(0)) > 0) {
				return true;
			}
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	public static String format(String str, int length, String extension) {
		while (str.length() < length) {
			str = extension + str;
		}
		return str;
	}

}