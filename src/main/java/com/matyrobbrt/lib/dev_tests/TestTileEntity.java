package com.matyrobbrt.lib.dev_tests;

import com.matyrobbrt.lib.MatyLib;
import com.matyrobbrt.lib.annotation.SyncValue;
import com.matyrobbrt.lib.registry.annotation.AutoBlockItem;
import com.matyrobbrt.lib.registry.annotation.RegisterBlock;
import com.matyrobbrt.lib.registry.annotation.RegisterTileEntityType;
import com.matyrobbrt.lib.registry.annotation.RegistryHolder;
import com.matyrobbrt.lib.tile_entity.BaseTileEntity;
import com.matyrobbrt.lib.wrench.IWrenchBehaviour;
import com.matyrobbrt.lib.wrench.IWrenchUsable;
import com.matyrobbrt.lib.wrench.WrenchResult;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

@RegistryHolder(modid = MatyLib.MOD_ID)
class TestTileEntity extends BaseTileEntity implements ITickableTileEntity {

	@AutoBlockItem
	@RegisterBlock("test")
	public static final TestBlock TEST_BLOCK = new TestBlock(AbstractBlock.Properties.copy(Blocks.IRON_BARS));

	public static final class TestBlock extends Block implements IWrenchUsable {

		public TestBlock(Properties properties) {
			super(properties);
		}

		@Override
		public boolean hasTileEntity(net.minecraft.block.BlockState state) {
			return true;
		}

		@Override
		public TileEntity createTileEntity(net.minecraft.block.BlockState state,
				net.minecraft.world.IBlockReader world) {
			return TE_TYPE.create();
		}

		@Override
		public IWrenchBehaviour getBehaviour() {
			return (stack, mode, player, state, pos, level) -> {
				System.out.println("yes");
				return WrenchResult.CONSUME;
			};
		}
	}

	@RegisterTileEntityType("test")
	public static final TileEntityType<TestTileEntity> TE_TYPE = TileEntityType.Builder
			.of(TestTileEntity::new, TEST_BLOCK).build(null);

	public TestTileEntity() {
		super(TE_TYPE);
	}

	@SyncValue(name = "whateverSync", onPacket = true)
	private int whatever;

	@Override
	public void tick() {
		if (level.isClientSide()) {
			whatever = 12;
			// sync(Direction.CLIENT_TO_SERVER);
		} else {
			// System.out.println(whatever);
		}
	}

}
