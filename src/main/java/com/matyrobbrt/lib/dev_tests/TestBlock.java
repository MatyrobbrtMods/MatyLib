package com.matyrobbrt.lib.dev_tests;

import com.matyrobbrt.lib.multiblock.MultiblockDriver;
import com.matyrobbrt.lib.multiblock.MultiblockHelper;
import com.matyrobbrt.lib.wrench.IWrenchBehaviour;
import com.matyrobbrt.lib.wrench.IWrenchUsable;
import com.matyrobbrt.lib.wrench.WrenchResult;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

class TestBlock extends BaseEntityBlock implements IWrenchUsable {

	public TestBlock(Properties properties) {
		super(properties);
	}

	@Override
	public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
		if (pLevel.isClientSide()) { return; }

		MultiblockDriver<TestMultiblock> mbDriver = TestWSD.getInstance(pLevel).getDriver();
		mbDriver.createOrUpdate(mbDriver.createId(), new TestMultiblock()).addBlockPos(pPos);
		MultiblockHelper.merge(pLevel, pPos, mbDriver);
	}

	@Override
	public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
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

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state,
			BlockEntityType<T> type) {
		return !level.isClientSide() && state != null
				? BaseEntityBlock.createTickerHelper(type, TestModule.TE_TYPE, (level$, pos, state$, te) -> te.tick())
				: null;
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos p_153215_, BlockState p_153216_) {
		return new TestTileEntity(p_153215_, p_153216_);
	}

}
