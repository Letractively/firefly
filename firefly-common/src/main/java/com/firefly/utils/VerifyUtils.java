package com.firefly.utils;

abstract public class VerifyUtils {

	public static boolean simpleWildcardMatch(String pattern, String str) {
		return wildcardMatch(pattern, str, "*");
	}

	public static boolean wildcardMatch(String pattern, String str,
			String wildcard) {
		if (isEmpty(pattern) || isEmpty(str)) {
			return false;
		}
		final boolean startWith = pattern.startsWith(wildcard);
		final boolean endWith = pattern.endsWith(wildcard);
		String[] array = StringUtils.split(pattern, wildcard);
		int currentIndex = -1;
		int lastIndex = -1;
		switch (array.length) {
		case 0:
			return true;
		case 1:
			currentIndex = str.indexOf(array[0]);
			if (startWith && endWith) {
				return currentIndex >= 0;
			}
			if (startWith) {
				return currentIndex + array[0].length() == str.length();
			}
			if (endWith) {
				return currentIndex == 0;
			}
			return str.equals(pattern);
		default:
			for (String part : array) {
				currentIndex = str.indexOf(part);
				if (currentIndex > lastIndex) {
					lastIndex = currentIndex;
					continue;
				}
				return false;
			}
			return true;
		}
	}

	public static boolean isNumeric(String str) {
		if (isEmpty(str)) {
			return false;
		}
		for (int i = 0; i < str.length(); i++) {
			if (isDigit(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}
	
	public static boolean isDouble(String str) {
		if (isEmpty(str)) {
			return false;
		}
		int point = 0;
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if(c == '.') {
				point++;
			} else if (isDigit(c) == false) {
				return false;
			}
		}
		
		return point == 1;
	}

	public static boolean isDigit(char ch) {
		return ch >= '0' && ch <= '9';
	}

	public static boolean isNotEmpty(Long o) {
		return o != null && StringUtils.hasText(o.toString());
	}

	public static boolean isNotEmpty(Integer o) {
		return o != null && StringUtils.hasText(o.toString());
	}

	public static boolean isNotEmpty(String o) {
		return StringUtils.hasText(o);
	}

	public static boolean isEmpty(Long o) {
		return !isNotEmpty(o);
	}

	public static boolean isEmpty(Integer o) {
		return !isNotEmpty(o);
	}

	public static boolean isEmpty(String o) {
		return !isNotEmpty(o);
	}
}
