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

package com.matyrobbrt.lib.compat.curios;

import java.util.function.Predicate;

import org.apache.commons.lang3.tuple.ImmutableTriple;

import com.google.common.collect.Multimap;
import com.matyrobbrt.lib.capability.SimpleCapabilityProvider;
import com.matyrobbrt.lib.util.ModIDs;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fml.InterModComms;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotTypeMessage;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.capability.ICurio;

class CuriosSetup extends CurioHandler {

	public static void sendIMC() {
		InterModComms.sendTo(ModIDs.CURIOS, SlotTypeMessage.REGISTER_TYPE,
				() -> SlotTypePreset.CHARM.getMessageBuilder().build());
		InterModComms.sendTo(ModIDs.CURIOS, SlotTypeMessage.REGISTER_TYPE,
				() -> SlotTypePreset.RING.getMessageBuilder().build());
		InterModComms.sendTo(ModIDs.CURIOS, SlotTypeMessage.REGISTER_TYPE,
				() -> SlotTypePreset.BELT.getMessageBuilder().build());
		InterModComms.sendTo(ModIDs.CURIOS, SlotTypeMessage.REGISTER_TYPE,
				() -> SlotTypePreset.BODY.getMessageBuilder().build());
		InterModComms.sendTo(ModIDs.CURIOS, SlotTypeMessage.REGISTER_TYPE,
				() -> SlotTypePreset.HEAD.getMessageBuilder().build());
		InterModComms.sendTo(ModIDs.CURIOS, SlotTypeMessage.REGISTER_TYPE,
				() -> SlotTypePreset.NECKLACE.getMessageBuilder().build());
	}

	public static final class Wrapper implements ICurio {

		private final ItemStack stack;

		Wrapper(ItemStack stack) {
			this.stack = stack;
		}

		private CurioItem getItem() { return (CurioItem) stack.getItem(); }

		@Override
		public void curioTick(String identifier, int index, LivingEntity entity) {
			getItem().onWornTick(stack, entity);
		}

		@Override
		public void onEquip(String identifier, int index, LivingEntity entity) {
			getItem().onEquipped(stack, entity);
		}

		@Override
		public void onUnequip(String identifier, int index, LivingEntity entity) {
			getItem().onUnequipped(stack, entity);
		}

		@Override
		public boolean canEquip(String identifier, LivingEntity entity) {
			return getItem().canEquip(stack, entity);
		}

		@Override
		public Multimap<Attribute, AttributeModifier> getAttributeModifiers(String identifier) {
			return getItem().getEquippedAttributeModifiers(stack);
		}

		@Override
		public boolean canSync(String identifier, int index, LivingEntity livingEntity) {
			return true;
		}

		@Override
		public boolean canRightClickEquip() {
			return true;
		}

		@Override
		public ItemStack getStack() { return stack; }
	}

	@Override
	protected ICapabilityProvider initCap(ItemStack stack) {
		return new SimpleCapabilityProvider<ICurio>(CuriosCapability.ITEM, new Wrapper(stack));
	}

	@Override
	public ItemStack findItem(Item item, LivingEntity entity) {
		return CuriosApi.getCuriosHelper().findEquippedCurio(item, entity).map(ImmutableTriple::getRight)
				.orElse(ItemStack.EMPTY);
	}

	@Override
	public ItemStack findItem(Predicate<ItemStack> pred, LivingEntity entity) {
		return CuriosApi.getCuriosHelper().findEquippedCurio(pred, entity).map(ImmutableTriple::getRight)
				.orElse(ItemStack.EMPTY);
	}

}
