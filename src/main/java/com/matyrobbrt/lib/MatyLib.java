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

package com.matyrobbrt.lib;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.matyrobbrt.lib.compat.curios.CuriosCompat;
import com.matyrobbrt.lib.compat.top.TheOneProbeCompat;
import com.matyrobbrt.lib.dev_tests.MatyLibDevTests;
import com.matyrobbrt.lib.network.matylib.MatyLibNetwork;
import com.matyrobbrt.lib.registry.annotation.AnnotationProcessor;
import com.matyrobbrt.lib.util.ModIDs;
import com.matyrobbrt.lib.wrench.DefaultWrenchBehaviours;
import com.matyrobbrt.lib.wrench.WrenchIMC;
import com.matyrobbrt.lib.wrench.WrenchItem;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;

@Mod(MatyLib.MOD_ID)
public class MatyLib extends ModSetup {

	private static boolean registered = false;

	public static MatyLib INSTANCE;

	public static boolean patchouliLoaded = false;
	public static boolean curiosLoaded = false;

	public static final Logger LOGGER = LogManager.getLogger();
	public static final String MOD_ID = "matylib";

	public static final AnnotationProcessor ANN_PROCESSOR = new AnnotationProcessor(MOD_ID);

	public MatyLib() {
		super(MOD_ID);
		if (!registered) {
			registered = true;
			INSTANCE = this;
		}
		ANN_PROCESSOR.afterInit(() -> {
			if (isProduction()) {
				MatyLibDevTests.unregisterTests();
			}
		});
	}

	@Override
	public AnnotationProcessor annotationProcessor() {
		return ANN_PROCESSOR;
	}

	@Override
	public void onCommonSetup(final FMLCommonSetupEvent event) {
		patchouliLoaded = ModList.get().isLoaded(ModIDs.PATCHOULI);
		curiosLoaded = ModList.get().isLoaded(ModIDs.CURIOS);

		MatyLibNetwork.init();
	}

	@Override
	public void onInterModEnqueue(InterModEnqueueEvent event) {
		if (ModList.get().isLoaded(ModIDs.THE_ONE_PROBE)) {
			TheOneProbeCompat.register();
		}
		if (ModList.get().isLoaded(ModIDs.CURIOS)) {
			CuriosCompat.register();
		} else {
			CuriosCompat.INSTANCE = new CuriosCompat.DefaultCurioHandler();
		}

		InterModComms.sendTo(MOD_ID, WrenchIMC.REGISTER_WRENCH_BEHAVIOUR_METHOD,
				() -> DefaultWrenchBehaviours.normalDismantle(Blocks.IRON_BLOCK));
	}

	public static final ItemGroup MATYLIB_TAB = new ItemGroup(ItemGroup.TABS.length, "matylib") {

		@Override
		public ItemStack makeIcon() {
			return WrenchItem.MATYLIB_WRENCH_ITEM.getDefaultInstance();
		}
	};

}
