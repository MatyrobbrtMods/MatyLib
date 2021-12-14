package com.matyrobbrt.lib.dev_tests;

import com.matyrobbrt.lib.MatyLib;
import com.matyrobbrt.lib.annotation.RL;
import com.matyrobbrt.lib.module.IModule;
import com.matyrobbrt.lib.module.Module;
import com.matyrobbrt.lib.module.ModuleHelper;
import com.matyrobbrt.lib.registry.annotation.AutoBlockItem;
import com.matyrobbrt.lib.registry.annotation.RegisterBlock;
import com.matyrobbrt.lib.registry.annotation.RegisterTileEntityType;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.tileentity.TileEntityType;

@Module(id = @RL(modid = MatyLib.MOD_ID, path = "test"))
public class TestModule extends ModuleHelper implements IModule {

	@AutoBlockItem
	@RegisterBlock("test")
	public static final TestBlock TEST_BLOCK = new TestBlock(AbstractBlock.Properties.copy(Blocks.IRON_BARS));

	@RegisterTileEntityType("test")
	public static final TileEntityType<TestTileEntity> TE_TYPE = TileEntityType.Builder
			.of(TestTileEntity::new, TEST_BLOCK).build(null);

}
