package com.matyrobbrt.lib.dev_tests;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.matyrobbrt.lib.multiblock.IMultiblock;
import com.matyrobbrt.lib.multiblock.IMultiblockConnector;
import com.matyrobbrt.lib.multiblock.MultiblockDriver;
import com.matyrobbrt.lib.multiblock.MultiblockHolder;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

final class TestMultiblock implements IMultiblock {

	public boolean isValid(MultiblockHolder<? super TestMultiblock> holder) {
		for (BlockPos pos : holder.getPositions()) {
			if (holder.containsBlockPos(pos.above())) { return true; }
		}
		return false;
	}

	public static final class Merger implements IMultiblockConnector<TestMultiblock> {

		@Override
		public void initialize(MultiblockDriver<TestMultiblock> driver, World level, TestMultiblock newMultiblock,
				int id) {
			driver.createOrUpdate(id, newMultiblock);
		}

		@Override
		public void merge(MultiblockDriver<TestMultiblock> driver, World level, TestMultiblock newMultiblock,
				TestMultiblock otherMultiblock) {
			Pair<Integer, MultiblockHolder<TestMultiblock>> otherHolder = driver
					.getHolderForMultiblock(otherMultiblock);
			MultiblockHolder<TestMultiblock> newHolder = driver.getHolderForMultiblock(newMultiblock).getRight();

			otherHolder.getRight().getPositions().forEach(newHolder::addBlockPos);
			driver.remove(otherHolder.getKey());
		}

		@Override
		public void distribute(MultiblockDriver<TestMultiblock> driver, World level, TestMultiblock original,
				List<Pair<Integer, Set<BlockPos>>> todo) {
		}

	}
}
