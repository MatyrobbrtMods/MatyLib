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

package com.matyrobbrt.lib.util;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.google.common.collect.Maps;
import com.matyrobbrt.lib.MatyLib;

import net.minecraft.block.Block;
import net.minecraft.item.AxeItem;

import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;

@EventBusSubscriber(modid = MatyLib.MOD_ID, bus = Bus.MOD)
public class StrippingMap {

	private static boolean registered = false;

	private static final Map<Supplier<Block>, Supplier<Block>> ENQUEUED_ADDING = new HashMap<>();

	public static void createStrippable(Supplier<Block> input, Supplier<Block> output) {
		ENQUEUED_ADDING.put(input, output);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onLoadComplete(final FMLLoadCompleteEvent event) {
		registerStrippables();
	}

	private static void registerStrippables() {
		if (!registered) {
			ENQUEUED_ADDING.forEach((input, output) -> {
				AxeItem.STRIPABLES = Maps.newHashMap(AxeItem.STRIPABLES);
				AxeItem.STRIPABLES.put(input.get(), output.get());
			});
			registered = true;
		}
	}

}
