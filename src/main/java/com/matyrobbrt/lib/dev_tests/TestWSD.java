package com.matyrobbrt.lib.dev_tests;

import com.matyrobbrt.lib.MatyLib;
import com.matyrobbrt.lib.dev_tests.TestMultiblock.Merger;
import com.matyrobbrt.lib.multiblock.IMultiblockComponent;
import com.matyrobbrt.lib.multiblock.MultiblockDriver;
import com.matyrobbrt.lib.multiblock.wsd.MultiblockDriverWSD;
import com.matyrobbrt.lib.util.helper.TernaryHelper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

final class TestWSD extends MultiblockDriverWSD<TestMultiblock> {

	public static final String ID = "test_multiblock";

	private TestWSD() {
	}

	public static TestWSD load(CompoundTag tag) {
		TestWSD wsd = new TestWSD();
		wsd.driver.load(tag);
		return wsd;
	}

	public static TestWSD getInstance(Level level) {
		return getInstance((ServerLevel) level);
	}

	public static TestWSD getInstance(ServerLevel level) {
		return level.getDataStorage().computeIfAbsent(TestWSD::load, TestWSD::new, ID);
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
