package com.matyrobbrt.lib.util.helper;

import java.util.function.Supplier;

public final class TernaryHelper {

	public static <T> T supplier(Supplier<T> sup) {
		return sup.get();
	}

}
