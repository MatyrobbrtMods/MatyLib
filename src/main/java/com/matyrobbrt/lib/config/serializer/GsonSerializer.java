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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.matyrobbrt.lib.config.ConfigUtils;
import com.matyrobbrt.lib.config.IConfigData;
import com.matyrobbrt.lib.config.annotation.Config;
import com.matyrobbrt.lib.util.Utils;

public class GsonSerializer<C extends IConfigData> implements IConfigSerializer<C> {

	private Config definition;
	private Class<C> configClass;
	private Gson gson;

	public GsonSerializer(Config definition, Class<C> configClass, Gson gson) {
		this.definition = definition;
		this.configClass = configClass;
		this.gson = gson;
	}

	public GsonSerializer(Config definition, Class<C> configClass) {
		this(definition, configClass, new GsonBuilder().setPrettyPrinting().create());
	}

	@Override
	public Supplier<Path> getConfigPath() {
		return () -> ConfigUtils.getConfigPath(definition).resolve(definition.name() + ".json");
	}

	@Override
	public void serialize(C config) throws SerializationException {
		Path configPath = getConfigPath().get();
		try {
			Files.createDirectories(configPath.getParent());
		} catch (IOException e1) {
			throw new SerializationException(e1);
		}
		try (BufferedWriter writer = Files.newBufferedWriter(configPath)) {
			gson.toJson(config, writer);
		} catch (IOException e) {
			throw new SerializationException(e);
		}
	}

	@Override
	public C deserialize() throws SerializationException {
		Path configPath = getConfigPath().get();
		if (Files.exists(configPath)) {
			try (BufferedReader reader = Files.newBufferedReader(configPath);) {
				return gson.fromJson(reader, configClass);
			} catch (IOException | JsonParseException e) {
				throw new SerializationException(e);
			}
		} else {
			return createDefault();
		}
	}

	@Override
	public C createDefault() {
		return Utils.constructUnsafely(configClass);
	}

	@Override
	public C deserialize(ByteArrayInputStream stream) throws SerializationException {
		try (InputStreamReader reader = new InputStreamReader(stream)) {
			return gson.fromJson(reader, configClass);
		} catch (IOException | JsonParseException e) {
			throw new SerializationException(e);
		}
	}

}
