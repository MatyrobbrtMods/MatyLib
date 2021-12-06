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
