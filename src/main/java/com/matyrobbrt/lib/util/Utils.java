package com.matyrobbrt.lib.util;

import java.util.Optional;

public class Utils {

	/**
	 * Checks if an object is an instance of a class. <br>
	 * If the object is an instance, returns an {@link Optional} containing the
	 * {@code object} casted to {@code clazz}
	 * 
	 * @param <T>
	 * @param object
	 * @param clazz
	 * @return
	 */
	public static <T> Optional<T> instanceOf(Object object, Class<T> clazz) {
		if (clazz.isInstance(object)) { return Optional.of(clazz.cast(object)); }
		return Optional.ofNullable(null);
	}

}
