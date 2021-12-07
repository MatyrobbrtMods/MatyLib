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
import com.matyrobbrt.lib.network.matylib.MatyLibNetwork;
import com.matyrobbrt.lib.util.ModIDs;
import com.matyrobbrt.lib.version.VersionManager;

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

	public MatyLib() {
		super(MOD_ID);
		if (!registered) {
			registered = true;
			INSTANCE = this;
		}
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

		InterModComms.sendTo(MOD_ID, VersionManager.VERSIONS_METHOD,
				() -> "https://raw.githubusercontent.com/Matyrobbrt/MatyLib/1.16.5/versions.json");
	}

}
