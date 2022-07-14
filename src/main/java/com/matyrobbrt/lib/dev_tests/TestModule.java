package com.matyrobbrt.lib.dev_tests;

import com.matyrobbrt.lib.MatyLib;
import com.matyrobbrt.lib.annotation.RL;
import com.matyrobbrt.lib.module.IModule;
import com.matyrobbrt.lib.module.Module;
import com.matyrobbrt.lib.module.ModuleHelper;
import com.matyrobbrt.lib.registry.annotation.AutoBlockItem;
import com.matyrobbrt.lib.registry.annotation.RegisterBlock;
import com.matyrobbrt.lib.registry.annotation.RegisterBlockEntityType;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;

//@Module(id = @RL(modid = MatyLib.MOD_ID, path = "test"))
public class TestModule extends ModuleHelper implements IModule {

	@AutoBlockItem
	@RegisterBlock("test")
	public static final TestBlock TEST_BLOCK = new TestBlock(BlockBehaviour.Properties.copy(Blocks.IRON_BARS));

	@RegisterBlockEntityType("test")
	public static final BlockEntityType<TestTileEntity> TE_TYPE = BlockEntityType.Builder
			.of(TestTileEntity::new, TEST_BLOCK).build(null);

}
