package com.matyrobbrt.lib.wrench;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.matyrobbrt.lib.MatyLib;

import net.minecraft.block.Blocks;

import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;

@EventBusSubscriber(modid = MatyLib.MOD_ID, bus = Bus.MOD)
public class WrenchIMC {

	public static final String REGISTER_WRENCH_BEHAVIOUR_METHOD = "registerWrenchBehaviour";

	private static final List<IWrenchBehaviour> BEHAVIOURS = new LinkedList<>();

	public static List<IWrenchBehaviour> getBehaviours() { return new ArrayList<>(BEHAVIOURS); }

	static {
		BEHAVIOURS.add(DefaultWrenchBehaviours.normalDismantle(Blocks.IRON_BLOCK));
	}

	@SubscribeEvent
	public static void processIMC(final InterModProcessEvent event) {
		event.getIMCStream().filter(msg -> msg.getMethod() == REGISTER_WRENCH_BEHAVIOUR_METHOD).forEach(imc -> {
			Object msg = imc.getMessageSupplier().get();
			if (msg instanceof IWrenchBehaviour) {
				BEHAVIOURS.add((IWrenchBehaviour) msg);
			}
		});
	}

}
