package com.firefly.utils;

import java.util.Calendar;

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
		int sz = str.length();
		for (int i = 0; i < sz; i++) {
			if (isDigit(str.charAt(i)) == false) {
				return false;
			}
		}
		return true;
	}

	public static boolean isDigit(char ch) {
		return ch >= '0' && ch <= '9';
	}

	public static boolean isNotEmpty(Long o) {
		return o != null && o.toString().length() > 0;
	}

	public static boolean isNotEmpty(Integer o) {
		return o != null && o.toString().length() > 0;
	}

	public static boolean isNotEmpty(String o) {
		return o != null && o.toString().length() > 0;
	}

	public static boolean isEmpty(Long o) {
		return o == null || o.toString().length() == 0;
	}

	public static boolean isEmpty(Integer o) {
		return o == null || o.toString().length() == 0;
	}

	public static boolean isEmpty(String o) {
		return o == null || o.toString().length() == 0;
	}

	/**
	 * 判断是否时间类型
	 *
	 * @param clazz
	 * @return
	 */
	public static boolean isDateLike(Class<?> clazz) {
		return Calendar.class.isAssignableFrom(clazz)
				|| java.util.Date.class.isAssignableFrom(clazz)
				|| java.sql.Date.class.isAssignableFrom(clazz)
				|| java.sql.Time.class.isAssignableFrom(clazz);
	}

	/**
	 * @return 当前对象是否为数字类型
	 */
	public static boolean isNumber(Class<?> clazz) {
		return Number.class.isAssignableFrom(clazz) || clazz.isPrimitive()
				&& clazz != boolean.class && clazz != char.class;
	}

	/**
	 * @return 当前对象是否为布尔
	 */
	public static boolean isBoolean(Class<?> clazz) {
		return clazz == boolean.class || clazz == Boolean.class;
	}

	/**
	 * @return 当前对象是否为字符
	 */
	public static boolean isChar(Class<?> clazz) {
		return clazz == char.class || clazz == Character.class;
	}
}
