package com.matyrobbrt.lib.wrench;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@FunctionalInterface
public interface IWrenchBehaviour {

	ActionResultType executeAction(ItemStack wrench, WrenchMode mode, PlayerEntity player, BlockState state,
			BlockPos pos, World level);

}
