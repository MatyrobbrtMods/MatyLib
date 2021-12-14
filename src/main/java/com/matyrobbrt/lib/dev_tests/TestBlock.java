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
