package com.matyrobbrt.lib.scheduler;

import java.util.function.Consumer;

import javax.annotation.concurrent.ThreadSafe;

import com.matyrobbrt.lib.MatyLib;

import net.minecraft.server.MinecraftServer;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.Phase;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

@ThreadSafe
@Mod.EventBusSubscriber(modid = MatyLib.MOD_ID, bus = Bus.MOD)
public final class ServerTickScheduler extends Scheduler<MinecraftServer> {

	private void serverTicks(final TickEvent.ServerTickEvent event) {
		if (event.phase == Phase.START) {
			Consumer<MinecraftServer> action = scheduledActions.poll();
			do {
				if (action != null) {
					action.accept(ServerLifecycleHooks.getCurrentServer());
					action = scheduledActions.poll();
				}
			} while (action != null);
		}
	}

	public static void scheduleAction(Consumer<MinecraftServer> action) {
		INSTANCE.scheduledActions.add(action);

		if (INSTANCE.scheduledActions.size() > QUEUE_OVERFLOW_LIMIT) {
			MatyLib.LOGGER.warn(
					"Too many server tick actions are queued! Currently at {} actions. Would have added a new action '{}'.",
					INSTANCE.scheduledActions.size(), action);
			MatyLib.LOGGER.catching(new IllegalStateException("ServerTicks queue overflow, over the limit of "
					+ QUEUE_OVERFLOW_LIMIT + "! Clearing all actions!"));
			INSTANCE.scheduledActions.clear();
		}
	}

	private static final ServerTickScheduler INSTANCE = new ServerTickScheduler();

	private ServerTickScheduler() {
		MinecraftForge.EVENT_BUS.addListener(this::serverTicks);
	}

	public static void classLoad(final FMLCommonSetupEvent event) {
		// just to class load
	}
}
