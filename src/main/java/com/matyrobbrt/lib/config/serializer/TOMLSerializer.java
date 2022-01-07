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

package com.matyrobbrt.lib.config.serializer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.function.Supplier;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.google.common.base.Joiner;
import com.matyrobbrt.lib.config.ConfigUtils;
import com.matyrobbrt.lib.config.IConfigData;
import com.matyrobbrt.lib.config.annotation.Config;
import com.matyrobbrt.lib.config.annotation.ConfigEntry;
import com.matyrobbrt.lib.util.Utils;

import net.minecraftforge.fml.loading.FMLPaths;

public class TOMLSerializer<C extends IConfigData> implements IConfigSerializer<C> {

	private CommentedFileConfig configFile;
	private final Config configDefinition;
	private final Class<C> configClass;

	private final C defaultInstace;

	public TOMLSerializer(Config configDefinition, Class<C> configClass) {
		this.configDefinition = configDefinition;
		this.configClass = configClass;
		this.defaultInstace = createDefault();
	}

	@Override
	public Supplier<Path> getConfigPath() {
		return () -> ConfigUtils.getConfigPath(configDefinition).resolve(configDefinition.name() + ".toml");
	}

	@Override
	public void serialize(C config) throws SerializationException {
		safeSerialize(config);
	}

	private void safeSerialize(C config) throws SerializationException {
		try {
			Path configPath = getConfigPath().get();
			if (!configPath.toFile().exists()) {
				Files.createFile(configPath);
			}
			final CommentedFileConfig configFile = CommentedFileConfig.of(configPath);
			for (Field field : config.getClass().getFields()) {
				field.setAccessible(true);
				if (!field.isAnnotationPresent(ConfigEntry.class)) {
					continue;
				}
				ConfigEntry entry = field.getAnnotation(ConfigEntry.class);
				String entryName = getValueName(entry);
				configFile.set(entryName, field.get(config));
				StringBuilder comment = new StringBuilder();
				if (entry.comments().length > 0 && !entry.comments()[0].isEmpty()) {
					comment.append(LINE_JOINER.join(entry.comments()));
				}
				if (entry.commentDefaultValue()) {
					comment.append(System.getProperty("line.separator"));
					comment.append("default: " + field.get(defaultInstace));
				}
				if (!comment.toString().isEmpty()) {
					configFile.setComment(entryName, comment.toString());
				}
			}
			configFile.save();
		} catch (IOException | IllegalArgumentException | IllegalAccessException e) {
			throw new SerializationException(e);
		}
	}

	@Override
	public C deserialize() throws SerializationException {
		try {
			Path path = getConfigPath().get();
			if (!path.toFile().exists()) {
				Files.createFile(path);
				serialize(defaultInstace);
				return defaultInstace;
			}
		} catch (IOException e) {
			throw new SerializationException(e);
		}

		if (this.configFile == null) {
			this.configFile = CommentedFileConfig.builder(getConfigPath().get()).build();
		}
		return deserialize(configFile);
	}

	public C deserialize(CommentedFileConfig configFile) throws SerializationException {
		C config = defaultInstace;
		configFile.load();
		for (Field field : config.getClass().getFields()) {
			field.setAccessible(true);
			if (!field.isAnnotationPresent(ConfigEntry.class)) {
				continue;
			}
			ConfigEntry entry = field.getAnnotation(ConfigEntry.class);
			String entryName = getValueName(entry);
			try {
				Object value = field.get(defaultInstace);
				if (configFile.contains(entryName)) {
					value = configFile.get(entryName);
				}
				field.set(config, value);
			} catch (IllegalArgumentException | IllegalAccessException e) {
				throw new SerializationException(e);
			}
		}

		return config;
	}

	public static String getValueName(ConfigEntry entry) {
		String category = entry.category().isEmpty() ? "" : entry.category() + ".";
		return category + entry.name();
	}

	@Override
	public C deserialize(ByteArrayInputStream stream) throws SerializationException {
		C configInstance = null;
		try {
			Path outputPath = FMLPaths.GAMEDIR.get().resolve(".config_cache/").resolve(configDefinition.folder())
					.resolve(configDefinition.name() + ".toml");
			Files.createDirectories(outputPath.getParent());
			if (outputPath.toFile().exists()) {
				Files.deleteIfExists(outputPath);
			}
			try {
				Files.createFile(outputPath);
				Files.copy(stream, outputPath, StandardCopyOption.REPLACE_EXISTING);
			} catch (FileAlreadyExistsException e) {}
			configInstance = deserialize(CommentedFileConfig.of(outputPath));
		} catch (IOException e) {
			throw new SerializationException(e);
		}
		return configInstance;
	}

	@Override
	public C createDefault() {
		return Utils.constructUnsafely(configClass);
	}

	private static final Joiner LINE_JOINER = Joiner.on("\n");

}
