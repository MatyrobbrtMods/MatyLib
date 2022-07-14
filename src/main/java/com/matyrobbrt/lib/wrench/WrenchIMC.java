/**
 * This file is part of the MatyLib Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2022 Matyrobbrt
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.matyrobbrt.lib.MatyLib;

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
		BEHAVIOURS.add((wrench, mode, player, state, pos, level) -> state.getBlock()instanceof IWrenchUsable usable
				? usable.getBehaviour().executeAction(wrench, mode, player, state, pos, level)
				: WrenchResult.FAIL);
	}

	@SubscribeEvent
	public static void processIMC(final InterModProcessEvent event) {
		event.getIMCStream().filter(msg -> msg.method() == REGISTER_WRENCH_BEHAVIOUR_METHOD).forEach(imc -> {
			Object msg = imc.messageSupplier().get();
			if (msg instanceof IWrenchBehaviour) {
				BEHAVIOURS.add((IWrenchBehaviour) msg);
			}
		});
	}

}
