package com.matyrobbrt.lib.keybind;

import java.util.ArrayList;
import java.util.List;

import com.matyrobbrt.lib.MatyLib;

import net.minecraft.client.Minecraft;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = MatyLib.MOD_ID, bus = Bus.FORGE, value = Dist.CLIENT)
public class KeyBindingHandler {

	public static final List<BaseKeyBinding> HANDLED_KEY_BINDINGS = new ArrayList<>();

	@SubscribeEvent
	public static void clientTick(final ClientTickEvent event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.screen == null) {
			HANDLED_KEY_BINDINGS.stream().filter(key -> key.isDown() && key.checkEveryTick)
					.forEach(key -> key.run(mc));
		}
	}

	@SubscribeEvent
	public static void onKeyPress(InputEvent.KeyInputEvent event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null)
			return;
		onInput(mc, event.getKey(), event.getAction());
	}

	@SubscribeEvent
	public static void onMouseClick(InputEvent.MouseInputEvent event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level == null)
			return;
		onInput(mc, event.getButton(), event.getAction());
	}

	private static void onInput(Minecraft mc, int keyCode, int action) {
		if (mc.screen == null) {
			HANDLED_KEY_BINDINGS.stream().filter(key -> !key.checkEveryTick && key.isDown())
					.forEach(key -> key.run(mc));
		}
	}
}
