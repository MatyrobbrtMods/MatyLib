package com.matyrobbrt.lib.wrench;

import com.matyrobbrt.lib.MatyLib;
import com.matyrobbrt.lib.registry.annotation.RegisterItem;
import com.matyrobbrt.lib.registry.annotation.RegistryHolder;
import com.matyrobbrt.lib.registry.builder.ItemBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;

@RegistryHolder(modid = MatyLib.MOD_ID)
public class WrenchItem extends Item {

	@RegisterItem("wrench")
	public static final WrenchItem TEST_WRENCH_ITEM = new ItemBuilder<>(WrenchItem::new).tab(ItemGroup.TAB_MISC)
			.build();

	public WrenchItem(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public ActionResultType useOn(ItemUseContext pContext) {
		if (pContext.getLevel().isClientSide()) { return super.useOn(pContext); }
		System.out.println(WrenchIMC.getBehaviours());
		BlockState clickedState = pContext.getLevel().getBlockState(pContext.getClickedPos());
		for (IWrenchBehaviour behaviour : WrenchIMC.getBehaviours()) {
			ActionResultType action = behaviour.executeAction(pContext.getItemInHand(), WrenchMode.DISMANTALE,
					pContext.getPlayer(), clickedState, pContext.getClickedPos(), pContext.getLevel());
			if (action == ActionResultType.CONSUME) {
				return ActionResultType.CONSUME;
			} else if (action == ActionResultType.PASS) {
				continue;
			} else if ((action == ActionResultType.SUCCESS) || (action == ActionResultType.FAIL)) {
				break;
			}
		}
		return super.useOn(pContext);
	}

}
