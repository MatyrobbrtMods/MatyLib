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

import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ResourceLocation;

class TestTileEntity extends BaseTileEntity implements ITickableTileEntity, IMultiblockComponent {

	public TestTileEntity() {
		super(TestModule.TE_TYPE);
	}

	@SyncValue(name = "whateverSync", onPacket = true)
	private int whatever;

	@Override
	public void tick() {
		if (level.isClientSide()) {
			System.out.println(TestConfig.INSTANCE.get().test);
		} else {
			System.out.println(TestConfig.INSTANCE.get().test);
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
