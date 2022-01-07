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

import com.matyrobbrt.lib.multiblock.MultiblockDriver;
import com.matyrobbrt.lib.multiblock.MultiblockHelper;
import com.matyrobbrt.lib.wrench.IWrenchBehaviour;
import com.matyrobbrt.lib.wrench.IWrenchUsable;
import com.matyrobbrt.lib.wrench.WrenchResult;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

class TestBlock extends Block implements IWrenchUsable {

	public TestBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean hasTileEntity(net.minecraft.block.BlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(net.minecraft.block.BlockState state, net.minecraft.world.IBlockReader world) {
		return TestModule.TE_TYPE.create();
	}

	@Override
	public void setPlacedBy(World pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
		if (pLevel.isClientSide()) { return; }

		MultiblockDriver<TestMultiblock> mbDriver = TestWSD.getInstance(pLevel).getDriver();
		mbDriver.createOrUpdate(mbDriver.createId(), new TestMultiblock()).addBlockPos(pPos);
		MultiblockHelper.merge(pLevel, pPos, mbDriver);
	}

	@Override
	public void onRemove(BlockState pState, World pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
		if (pLevel.isClientSide()) { return; }

		MultiblockHelper.onBlockRemoved(TestWSD.getInstance(pLevel).getDriver(), pPos);
	}

	@Override
	public IWrenchBehaviour getBehaviour() {
		return (stack, mode, player, state, pos, level) -> {
			System.out.println("yes");
			return WrenchResult.CONSUME;
		};
	}

}
