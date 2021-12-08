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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import net.minecraftforge.common.capabilities.ICapabilityProvider;

public class CuriosCompat {

	public static CurioHandler INSTANCE;

	public static final Logger LOGGER = LogManager.getLogger();

	private static boolean registered;

	public static void register() {
		if (registered) { return; }
		registered = true;
		CuriosSetup.sendIMC();
		INSTANCE = new CuriosSetup();
		LOGGER.info("Found Curios! Enabled support!");
	}

	public static ICapabilityProvider initBaubleCap(ItemStack stack) {
		if (INSTANCE != null) { return INSTANCE.initCap(stack); }
		return null;
	}

	public static final class DefaultCurioHandler extends CurioHandler {

		@Override
		protected ICapabilityProvider initCap(ItemStack stack) {
			return null;
		}

		@Override
		public ItemStack findItem(Item item, LivingEntity entity) {
			return ItemStack.EMPTY;
		}

		@Override
		public ItemStack findItem(Predicate<ItemStack> pred, LivingEntity entity) {
			return ItemStack.EMPTY;
		}
	}

}
