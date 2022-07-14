/**
 * This file is part of the MatyLib Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2022 Matyrobbrt
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

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

/**
 * Interface that connects multiple multiblocks together
 *
 * @param <M>
 */
public interface IMultiblockConnector<M extends IMultiblock> {

	/**
	 * Initialize a new multiblock with a single block
	 */
	void initialize(MultiblockDriver<M> driver, Level level, M newMultiblock, int id);

	/**
	 * Merge the other multiblock into the main one. The given multiblocks are
	 * guaranteed to be compatible
	 */
	void merge(MultiblockDriver<M> driver, Level level, M newMultiblock, M otherMultiblock);

	/**
	 * Take an original multiblock and distribute it to the multiblocks in the todo
	 * list
	 */
	void distribute(MultiblockDriver<M> driver, Level level, M original, List<Pair<Integer, Set<BlockPos>>> todo);

}
