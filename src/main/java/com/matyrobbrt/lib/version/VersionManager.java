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

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;

import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;

@EventBusSubscriber(modid = MatyLib.MOD_ID, bus = Bus.MOD)
public class VersionManager {

	public static final String VERSIONS_METHOD = "versions";

	private static final Map<String, VersionList> MOD_VERSIONS = new HashMap<>();

	@SubscribeEvent
	public static void processIMC(final InterModProcessEvent event) {
		event.getIMCStream().filter(msg -> msg.getMethod() == VERSIONS_METHOD).forEach(imc -> {
			Object msg = imc.getMessageSupplier().get();
			if (msg instanceof String) {
				VersionList version = processVersion((String) msg);
				if (version != null) {
					MOD_VERSIONS.put(imc.getSenderModId(), version);
				}
			}
		});
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
			PlayerEntity player = event.getPlayer();
			ModList.get().getModFiles().forEach(modFile -> {
				modFile.getMods().forEach(modInfo -> {
					if (VersionManager.MOD_VERSIONS.containsKey(modInfo.getModId())) {
						VersionList versionList = VersionManager.MOD_VERSIONS.get(modInfo.getModId());
						versionList.latest_versions.forEach(version -> {
							if (version.minecraftVersion.equalsIgnoreCase(ModList.get().getModFileById("minecraft")
									.getMods().get(0).getVersion().toString())) {
								if (new ComparableVersion(modInfo.getVersion().toString())
										.compareTo(new ComparableVersion(version.modVersion)) < 0) {
									player.sendMessage(new StringTextComponent("The mod " + ColourCodes.DARK_PURPLE
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
