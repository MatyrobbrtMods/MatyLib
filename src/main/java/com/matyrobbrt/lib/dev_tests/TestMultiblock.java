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

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.matyrobbrt.lib.multiblock.IMultiblock;
import com.matyrobbrt.lib.multiblock.IMultiblockConnector;
import com.matyrobbrt.lib.multiblock.MultiblockDriver;
import com.matyrobbrt.lib.multiblock.MultiblockHolder;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

final class TestMultiblock implements IMultiblock {

	public boolean isValid(MultiblockHolder<? super TestMultiblock> holder) {
		for (BlockPos pos : holder.getPositions()) {
			if (holder.containsBlockPos(pos.above())) { return true; }
		}
		return false;
	}

	public static final class Merger implements IMultiblockConnector<TestMultiblock> {

		@Override
		public void initialize(MultiblockDriver<TestMultiblock> driver, Level level, TestMultiblock newMultiblock,
				int id) {
			driver.createOrUpdate(id, newMultiblock);
		}

		@Override
		public void merge(MultiblockDriver<TestMultiblock> driver, Level level, TestMultiblock newMultiblock,
				TestMultiblock otherMultiblock) {
			Pair<Integer, MultiblockHolder<TestMultiblock>> otherHolder = driver
					.getHolderForMultiblock(otherMultiblock);
			MultiblockHolder<TestMultiblock> newHolder = driver.getHolderForMultiblock(newMultiblock).getRight();

			otherHolder.getRight().getPositions().forEach(newHolder::addBlockPos);
			driver.remove(otherHolder.getKey());
		}

		@Override
		public void distribute(MultiblockDriver<TestMultiblock> driver, Level level, TestMultiblock original,
				List<Pair<Integer, Set<BlockPos>>> todo) {
		}

	}
}
