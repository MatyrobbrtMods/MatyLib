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

package com.matyrobbrt.lib.config;

import java.nio.file.Path;

import com.matyrobbrt.lib.config.annotation.Config;
import com.matyrobbrt.lib.util.helper.TernaryHelper;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.storage.FolderName;

import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.loading.FileUtils;
import net.minecraftforge.fml.server.ServerLifecycleHooks;

public class ConfigUtils {

	public static Path getDefaultConfigPath() { return FMLPaths.CONFIGDIR.get(); }

	public static Path getGamePath() { return FMLPaths.GAMEDIR.get(); }

	public static Path getConfigPath(Config config) {
		return config.folder().isEmpty() ? TernaryHelper.supplier(() -> {
			if (config.configType() == ModConfig.Type.SERVER) {
				return getServerConfigPath(ServerLifecycleHooks.getCurrentServer());
			} else {
				return getDefaultConfigPath();
			}
		}) : getGamePath().resolve(config.folder());
	}

	public static final FolderName SERVERCONFIG = new FolderName("serverconfig");

	public static Path getServerConfigPath(final MinecraftServer server) {
		final Path serverConfig = server.getWorldPath(SERVERCONFIG);
		FileUtils.getOrCreateDirectory(serverConfig, "serverconfig");
		return serverConfig;
	}

}
