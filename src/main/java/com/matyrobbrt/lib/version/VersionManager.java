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

package com.matyrobbrt.lib.version;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.apache.maven.artifact.versioning.ComparableVersion;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.matyrobbrt.lib.MatyLib;
import com.matyrobbrt.lib.util.ColourCodes;

import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;

import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;

@EventBusSubscriber(modid = MatyLib.MOD_ID, bus = Bus.MOD)
public class VersionManager {

	public static final String VERSIONS_METHOD = "versions";

	private static final Map<String, VersionList> MOD_VERSIONS = new HashMap<>();

	@SubscribeEvent
	public static void processIMC(final InterModProcessEvent event) {
		event.getIMCStream().filter(msg -> msg.method() == VERSIONS_METHOD).forEach(imc -> {
			Object msg = imc.messageSupplier().get();
			if (msg instanceof String string) {
				VersionList version = processVersion(string);
				if (version != null) {
					MOD_VERSIONS.put(imc.senderModId(), version);
				}
			}
		});
	}

	@SubscribeEvent
	public static void onPreInit(final FMLConstructModEvent event) {
		MOD_VERSIONS.put(MatyLib.MOD_ID,
				processVersion("https://raw.githubusercontent.com/Matyrobbrt/MatyLib/1.17.1/versions.json"));
	}

	private static final Gson GSON = new GsonBuilder().disableHtmlEscaping().excludeFieldsWithoutExposeAnnotation()
			.create();

	public static VersionList processVersion(String urlString) {
		try {
			URL url = new URL(urlString);
			URLConnection uc = url.openConnection();
			uc.setRequestProperty("X-Requested-With", "Curl");
			return GSON.fromJson(new JsonReader(new InputStreamReader(uc.getInputStream())), VersionList.class);
		} catch (IOException e) {}
		return null;
	}

	@EventBusSubscriber(modid = MatyLib.MOD_ID)
	public static final class EventHandler {

		@SubscribeEvent
		public static void handle(final PlayerLoggedInEvent event) {
			Player player = event.getPlayer();
			ModList.get().getModFiles().forEach(modFile -> {
				modFile.getMods().forEach(modInfo -> {
					if (VersionManager.MOD_VERSIONS.containsKey(modInfo.getModId())) {
						VersionList versionList = VersionManager.MOD_VERSIONS.get(modInfo.getModId());
						versionList.latest_versions.forEach(version -> {
							if (version.minecraftVersion.equalsIgnoreCase(ModList.get().getModFileById("minecraft")
									.getMods().get(0).getVersion().toString())) {
								if (new ComparableVersion(modInfo.getVersion().toString())
										.compareTo(new ComparableVersion(version.modVersion)) < 0) {
									player.sendMessage(new TextComponent("The mod " + ColourCodes.DARK_PURPLE
											+ modInfo.getModId() + ColourCodes.RESET
											+ " is outdated! The latest version is " + ColourCodes.BLUE
											+ version.modVersion + ColourCodes.RESET + ". Found version "
											+ ColourCodes.RED + modInfo.getVersion().toString() + ColourCodes.RESET
											+ ". You can download the newest version from " + ColourCodes.GOLD
											+ version.downloadLink), player.getUUID());
								}
							}
						});
					}
				});
			});
		}
	}

}
