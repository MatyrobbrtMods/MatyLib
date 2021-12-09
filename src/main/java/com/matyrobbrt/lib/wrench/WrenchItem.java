package com.matyrobbrt.lib.wrench;

import com.matyrobbrt.lib.MatyLib;
import com.matyrobbrt.lib.registry.annotation.RegisterItem;
import com.matyrobbrt.lib.registry.annotation.RegistryHolder;
import com.matyrobbrt.lib.registry.builder.ItemBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

@RegistryHolder(modid = MatyLib.MOD_ID)
public class WrenchItem extends Item {

	@RegisterItem("wrench")
	public static final WrenchItem MATYLIB_WRENCH_ITEM = new ItemBuilder<>(WrenchItem::new).tab(MatyLib.MATYLIB_TAB)
			.build();

	public WrenchItem(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public ActionResultType useOn(ItemUseContext pContext) {
		if (pContext.getLevel().isClientSide()) { return super.useOn(pContext); }
		BlockState clickedState = pContext.getLevel().getBlockState(pContext.getClickedPos());
		for (IWrenchBehaviour behaviour : WrenchIMC.getBehaviours()) {
			WrenchResult result = behaviour.executeAction(pContext.getItemInHand(), WrenchMode.DISMANTALE,
					pContext.getPlayer(), clickedState, pContext.getClickedPos(), pContext.getLevel());
			if (result == WrenchResult.CONSUME) {
				return ActionResultType.CONSUME;
			}
		}
		return super.useOn(pContext);
	}

}
