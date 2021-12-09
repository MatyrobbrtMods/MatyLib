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
