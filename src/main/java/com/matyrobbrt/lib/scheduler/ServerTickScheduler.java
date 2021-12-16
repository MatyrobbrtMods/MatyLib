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
import net.minecraftforge.server.ServerLifecycleHooks;

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
