package com.agario2.util;

public class ClassUtil {
	public static String getClassPath(Class<?> targetClass) {
		String path = String.valueOf(targetClass.getResource(targetClass.getSimpleName() + ".class"));

		int index = path.indexOf("com");
		int last = path.length() - ".class".length();

		return path.substring(index, last).replace("/", ".");
	}
}