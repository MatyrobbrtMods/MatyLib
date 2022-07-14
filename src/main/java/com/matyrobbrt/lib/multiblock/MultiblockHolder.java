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

import com.matyrobbrt.lib.nbt.BaseNBTSet;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.LongTag;

public class MultiblockHolder<M extends IMultiblock> {

	private final M multiblock;
	private final BaseNBTSet<BlockPos, LongTag> positions = new BaseNBTSet<>(pos -> LongTag.valueOf(pos.asLong()),
			nbt -> BlockPos.of(nbt.getAsLong()));

	public MultiblockHolder(final M multiblock) {
		this.multiblock = multiblock;
	}

	public BaseNBTSet<BlockPos, LongTag> getPositions() { return positions; }

	public void removeBlockPos(final BlockPos toRemove) {
		positions.remove(toRemove);
	}

	public void addBlockPos(final BlockPos pos) {
		positions.add(pos);
	}

	public boolean containsBlockPos(final BlockPos pos) {
		return positions.contains(pos);
	}

	public M getMultiblock() { return multiblock; }

	public void save(CompoundTag nbt) {
		nbt.put("positions", positions.serializeNBT());
	}

	public void load(CompoundTag nbt) {
		positions.deserializeNBT(nbt.getCompound("positions"));
	}

}
