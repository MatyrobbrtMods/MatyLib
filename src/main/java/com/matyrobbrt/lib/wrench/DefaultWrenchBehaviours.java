package com.matyrobbrt.lib.wrench;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;

public class DefaultWrenchBehaviours {

	public static final IWrenchBehaviour normalDismantle(Block... blocks) {
		return (wrench, mode, player, state, pos, level) -> {
			if ((mode != WrenchMode.DISMANTALE) || !Arrays.asList(blocks).contains(state.getBlock())
					|| level.isClientSide()) {
				return ActionResultType.PASS;
			}
			List<ItemStack> drops = state.getDrops(
					new LootContext.Builder((ServerWorld) level).withParameter(LootParameters.TOOL, ItemStack.EMPTY)
							.withParameter(LootParameters.ORIGIN, new Vector3d(pos.getX(), pos.getY(), pos.getZ())));
			drops.forEach(stack -> {
				ItemEntity item = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack);
				level.addFreshEntity(item);
			});
			level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
			return ActionResultType.CONSUME;
		};
	}

}
