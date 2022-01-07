/**
 * This file is part of the MatyLib Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2021 Matyrobbrt
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

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
