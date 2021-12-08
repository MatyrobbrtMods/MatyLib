package com.matyrobbrt.lib.dev_tests;

import static com.matyrobbrt.lib.MatyLib.ANN_PROCESSOR;

public class MatyLibDevTests {

	public static void unregisterTests() {
		ANN_PROCESSOR.unregisterRegistryClass(TestTileEntity.class);
	}

}
