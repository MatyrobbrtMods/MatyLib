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

import java.io.File;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.matyrobbrt.lib.config.annotation.Config;
import com.matyrobbrt.lib.config.serializer.IConfigSerializer;

public class ConfigHolder<T extends IConfigData> implements IConfigHolder<T> {

	private final Logger logger;
	private final Config definition;
	private final Class<T> configClass;
	private final IConfigSerializer<T> serializer;

	private T config;

	ConfigHolder(Config definition, Class<T> configClass, IConfigSerializer<T> serializer) {
		logger = LogManager.getLogger();

		this.definition = definition;
		this.configClass = configClass;
		this.serializer = serializer;
	}

	public Config getDefinition() { return definition; }

	@Override
	@Nonnull
	public Class<T> getConfigClass() { return configClass; }

	public IConfigSerializer<T> getSerializer() { return serializer; }

	@Override
	public void save() {
		try {
			serializer.serialize(config);
		} catch (IConfigSerializer.SerializationException e) {
			logger.error("Failed to save config '{}'", configClass, e);
		}
	}

	@Override
	public boolean load() {
		try {
			config = serializer.deserialize();
			config.validate();
			return true;
		} catch (IConfigSerializer.SerializationException | IConfigData.ValidationException e) {
			logger.error("Failed to load config '{}', using default!", configClass, e);
			resetToDefault();
			return false;
		}
	}

	@Override
	public T getConfig() { return config; }

	@Override
	public void resetToDefault() {
		config = serializer.createDefault();
		try {
			config.validate();
		} catch (IConfigData.ValidationException v) {
			throw new RuntimeException("The result of createDefault() was invalid!", v);
		}
	}

	@Override
	public void setConfig(T config) { this.config = config; }

	@Override
	public Logger getLogger() { return logger; }

	@Override
	public File getConfigFile() { return getSerializer().getConfigPath().get().toFile(); }

}
