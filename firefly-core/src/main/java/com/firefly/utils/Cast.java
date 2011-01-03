package com.firefly.utils;

abstract public class Cast {
	public interface ValueClassName {
		String INTEGER = Integer.class.getName();
		String LONG = Long.class.getName();
		String DOUBLE = Double.class.getName();
		String FLOAT = Float.class.getName();
		String BOOLEAN = Boolean.class.getName();
		String SHORT = Short.class.getName();
		String BYTE = Byte.class.getName();
		String CHARACTER = Character.class.getName();
		String STRING = String.class.getName();
	}

	@SuppressWarnings("unchecked")
	public static <T> T convert(String value, Class<T> c) {
		Object ret = null;
		if (c.getName().equals(ValueClassName.INTEGER)) {
			ret = Integer.parseInt(value);
		} else if (c.getName().equals(ValueClassName.LONG)) {
			ret = Long.parseLong(value);
		} else if (c.getName().equals(ValueClassName.DOUBLE)) {
			ret = Double.parseDouble(value);
		} else if (c.getName().equals(ValueClassName.FLOAT)) {
			ret = Float.parseFloat(value);
		} else if (c.getName().equals(ValueClassName.BOOLEAN)) {
			ret = Boolean.parseBoolean(value);
		} else if (c.getName().equals(ValueClassName.SHORT)) {
			ret = Short.parseShort(value);
		} else if (c.getName().equals(ValueClassName.BYTE)) {
			ret = Byte.parseByte(value);
		} else if (c.getName().equals(ValueClassName.STRING)) {
			ret = value;
		}
		return (T) ret;
	}
}
