package com.matyrobbrt.lib.dev_tests;

import com.matyrobbrt.lib.MatyLib;
import com.matyrobbrt.lib.dev_tests.TestMultiblock.Merger;
import com.matyrobbrt.lib.multiblock.IMultiblockComponent;
import com.matyrobbrt.lib.multiblock.MultiblockDriver;
import com.matyrobbrt.lib.multiblock.wsd.MultiblockDriverWSD;
import com.matyrobbrt.lib.util.helper.TernaryHelper;

import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

final class TestWSD extends MultiblockDriverWSD<TestMultiblock> {

	public static final String ID = "test_multiblock";

	private TestWSD() {
		super(ID);
	}

	public static TestWSD getInstance(World level) {
		return getInstance((ServerWorld) level);
	}

	public static TestWSD getInstance(ServerWorld level) {
		return level.getDataStorage().computeIfAbsent(TestWSD::new, ID);
	}

	private final MultiblockDriver<TestMultiblock> driver = new MultiblockDriver.Builder<TestMultiblock>()
			.deserializer(tag -> new TestMultiblock()).serializer((nbt, mb) -> nbt.putString("yea", "no"))
			.componentGetter((level, pos) -> level.getBlockEntity(pos) instanceof IMultiblockComponent
					? TernaryHelper.supplier(() -> {
						IMultiblockComponent componenet = (IMultiblockComponent) level.getBlockEntity(pos);
						if (componenet.getId()
								.equals(new ResourceLocation(MatyLib.MOD_ID, "test"))) { return componenet; }
						return null;
					})
					: null)
			.connector(new Merger()).mergeChecker((mb1, mb2) -> true).dirtySetter(driver -> setDirty()).build();

	@Override
	public MultiblockDriver<TestMultiblock> getDriver() { return driver; }

}
