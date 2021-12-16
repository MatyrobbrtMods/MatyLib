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

package com.matyrobbrt.lib.multiblock;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Sets;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public class MultiblockHelper {

	/**
	 * Try to merge the multiblock on the given position to adjacent compatible
	 * multiblocks. Return true if something was changed
	 * 
	 * @param <T>
	 * @param level
	 * @param thisPos
	 * @param driver
	 * @return
	 */
	public static <T extends IMultiblock> boolean merge(Level level, BlockPos thisPos, MultiblockDriver<T> driver) {
		BiFunction<Level, BlockPos, IMultiblockComponent> componenetGetter = driver.getComponentGetter();
		IMultiblockConnector<T> connector = driver.getConnector();
		BiPredicate<T, T> mergeChecker = driver.getMergeChecker();

		Set<Integer> adjacentGeneratorIds = new HashSet<>();
		for (Direction direction : Direction.values()) {
			BlockPos pos = thisPos.relative(direction);
			IMultiblockComponent component = componenetGetter.apply(level, pos);
			if (component != null) {
				int multiblockId = component.getMultiblockId();
				adjacentGeneratorIds.add(multiblockId);
			}
		}

		IMultiblockComponent thisComponent = componenetGetter.apply(level, thisPos);
		int thisId = thisComponent.getMultiblockId();
		T thisMb = driver.get(thisId);

		if (adjacentGeneratorIds.isEmpty()) {
			return false;
		} else if (adjacentGeneratorIds.size() == 1) {
			// Check compatibility
			int multiblockID = adjacentGeneratorIds.iterator().next();
			if (mergeChecker.test(driver.get(multiblockID), thisMb)) {
				// Compatible so we can simply join this new block to that network
				connector.merge(driver, level, driver.get(multiblockID), thisMb);
				thisComponent.setMultiblockId(multiblockID);
				return true;
			} else {
				return false;
			}
		} else {
			// Multiple networks available. Try to merge with the first compatible one
			boolean merged = false;
			for (Integer id : adjacentGeneratorIds) {
				T adjacentMb = driver.get(id);
				if (mergeChecker.test(thisMb, adjacentMb)) {
					// Compatible. Merge
					connector.merge(driver, level, thisMb, adjacentMb);
					merged = true;
				}
			}

			adjacentGeneratorIds.add(thisId);
			setBlocksToNetwork(level, thisPos, adjacentGeneratorIds, null, thisId, driver);

			return merged;
		}
	}

	/**
	 * Add a new block to the adjacent multiblocks (possibly merging adjacent
	 * multiblocks) The new block should not have a network id yet (set to -1!)
	 * 
	 * @param <T>
	 * @param level
	 * @param thisPos
	 * @param driver
	 * @param newMb
	 */
	public static <T extends IMultiblock> void addBlock(Level level, BlockPos thisPos, MultiblockDriver<T> driver,
			T newMb) {
		BiFunction<Level, BlockPos, IMultiblockComponent> componentGetter = driver.getComponentGetter();
		IMultiblockConnector<T> connector = driver.getConnector();

		int mbId = driver.createId();
		driver.createOrUpdate(mbId, newMb);
		connector.initialize(driver, level, newMb, mbId);
		IMultiblockComponent thisInfo = componentGetter.apply(level, thisPos);
		thisInfo.setMultiblockId(mbId);
		merge(level, thisPos, driver);
	}

	/**
	 * Finds a multiblock at the gives pos
	 * 
	 * @param <T>
	 * @param level
	 * @param pos
	 * @param driver
	 * @return all the positions the multiblock has
	 */
	public static <T extends IMultiblock> Set<BlockPos> findMultiblock(Level level, BlockPos pos,
			MultiblockDriver<T> driver) {
		Set<BlockPos> positions = new HashSet<>();
		IMultiblockComponent holder = driver.getComponentGetter().apply(level, pos);
		if (holder != null) {
			findMultiblockInt(level, pos, driver.getComponentGetter(), positions);
		}
		return positions;
	}

	private static <T extends IMultiblock> void findMultiblockInt(Level level, BlockPos pos,
			BiFunction<Level, BlockPos, IMultiblockComponent> getter, Set<BlockPos> positions) {
		positions.add(pos);
		for (Direction direction : Direction.values()) {
			BlockPos p = pos.relative(direction);
			if (!positions.contains(p)) {
				if (getter.apply(level, p) != null) {
					findMultiblockInt(level, p, getter, positions);
				}
			}
		}
	}

	private static <T extends IMultiblock> void setBlocksToNetwork(Level level, BlockPos pos, @Nonnull Set<Integer> ids,
			@Nullable Set<BlockPos> done, int newId, MultiblockDriver<T> driver) {
		IMultiblockComponent componenent = driver.getComponent(level, pos);
		if (componenent == null) { return; }

		if (done != null) {
			done.add(pos);
		}
		componenent.setMultiblockId(newId);

		for (Direction direction : Direction.values()) {
			BlockPos newC = pos.relative(direction);
			if (done == null || !done.contains(newC)) {
				IMultiblockComponent com = driver.getComponentGetter().apply(level, newC);
				if (com != null) {
					int id = com.getMultiblockId();
					if (id != newId && ids.contains(id)) {
						setBlocksToNetwork(level, newC, ids, done, newId, driver);
					}
				}
			}
		}
	}

	/**
	 * Removes a position from the multiblock
	 * 
	 * @param <T>
	 * @param level
	 * @param thisPos
	 * @param driver
	 */
	public static <T extends IMultiblock> void removeBlock(Level level, BlockPos thisPos, MultiblockDriver<T> driver) {
		BiFunction<Level, BlockPos, IMultiblockComponent> compoenentGetter = driver.getComponentGetter();
		IMultiblockComponent thisCompoenent = compoenentGetter.apply(level, thisPos);
		T thisData = driver.get(thisCompoenent.getMultiblockId());

		// Clear all networks adjacent to this one.
		for (Direction direction : Direction.values()) {
			BlockPos newC = thisPos.relative(direction);
			IMultiblockComponent componenet = compoenentGetter.apply(level, newC);
			if (componenet != null) {
				setBlocksToNetwork(level, newC, Collections.singleton(componenet.getMultiblockId()), null, -1, driver);
			}
		}

		List<Pair<Integer, Set<BlockPos>>> todo = new ArrayList<>(); // A list of networks we have to fix
		int idToUse = thisCompoenent.getMultiblockId();
		for (Direction direction : Direction.values()) {
			BlockPos newPos = thisPos.relative(direction);
			IMultiblockComponent componenet = compoenentGetter.apply(level, newPos);
			if (componenet != null) {
				if (componenet.getMultiblockId() == -1) {
					if (idToUse == -1) {
						idToUse = driver.createId();
					}
					Set<BlockPos> done = Sets.newHashSet();
					done.add(thisPos);
					setBlocksToNetwork(level, newPos, Collections.singleton(-1), done, idToUse, driver);
					done.remove(thisPos);
					todo.add(Pair.of(idToUse, done));

					idToUse = -1;
				}
			}
		}

		driver.getConnector().distribute(driver, level, thisData, todo);
	}

	public static <T extends IMultiblock> void onBlockRemoved(MultiblockDriver<T> driver, BlockPos pos) {
		Integer id = driver.getIDForPos(pos);
		if (id != null) {
			MultiblockHolder<T> holder = driver.getHolder(id);
			if (holder.getPositions().size() <= 1 && holder.getPositions().contains(pos)) {
				driver.remove(id);
			}
		}
	}

}
