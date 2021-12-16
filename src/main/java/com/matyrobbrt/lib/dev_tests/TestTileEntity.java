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
import com.matyrobbrt.lib.annotation.SyncValue;
import com.matyrobbrt.lib.multiblock.IMultiblockComponent;
import com.matyrobbrt.lib.tile_entity.BaseTileEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;

class TestTileEntity extends BaseTileEntity implements IMultiblockComponent {

	public TestTileEntity(BlockPos pos, BlockState state) {
		super(TestModule.TE_TYPE, pos, state);
	}

	@SyncValue(name = "whateverSync", onPacket = true)
	private int whatever;

	public void tick() {
		if (level.isClientSide()) {
			whatever = 13;
			sync(com.matyrobbrt.lib.network.matylib.SyncValuesMessage.Direction.CLIENT_TO_SERVER);
		} else {
			if (getMultiblockId() != -1) {
				System.out.println(TestWSD.getInstance(level).getDriver().get(getMultiblockId())
						.isValid(TestWSD.getInstance(level).getDriver().getHolder(getMultiblockId())));
			}
		}
	}

	@Override
	public ResourceLocation getId() { return new ResourceLocation(MatyLib.MOD_ID, "test"); }

	@Override
	public int getMultiblockId() {
		Integer inT = TestWSD.getInstance(level).getDriver().getIDForPos(worldPosition);
		return inT != null ? inT : -1;
	}

	@Override
	public void setMultiblockId(int id) {
	}
}
