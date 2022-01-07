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

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import com.electronwill.nightconfig.core.file.FileWatcher;
import com.ibm.icu.impl.Pair;
import com.matyrobbrt.lib.config.annotation.Config;
import com.matyrobbrt.lib.config.serializer.IConfigSerializer;
import com.matyrobbrt.lib.util.ModIDs;

import net.minecraft.util.ResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;

@Mod.EventBusSubscriber(modid = ModIDs.MATY_LIB)
public class ConfigManager {

	private static volatile Map<Pair<Class<? extends IConfigData>, ResourceLocation>, IConfigHolder<?>> CONFIGS = Collections
			.synchronizedMap(new ConcurrentHashMap<>());

	private ConfigManager() {
		throw new IllegalAccessError("Why create an instance of me?");
	}

	public static synchronized <T extends IConfigData> IConfigHolder<T> register(Class<T> configClass,
			ResourceLocation configName, IConfigSerializer.Factory<T> serializerFactory) {
		Objects.requireNonNull(configClass);
		Objects.requireNonNull(serializerFactory);

		if (containsKey(configClass)) {
			throw new RuntimeException(String.format("Config '%s' has already been registered!", configClass));
		}

		Config configDefinition = configClass.getAnnotation(Config.class);

		if (configDefinition == null) {
			throw new RuntimeException(String.format("No @Config annotation on the class %s!", configClass));
		}

		if (configDefinition.configType() == Type.CLIENT) {
			AtomicReference<IConfigHolder<T>> toReturn = new AtomicReference<>();
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
					() -> () -> toReturn.set(registerClient(configClass, configName, serializerFactory)));
			return toReturn.get();
		}

		IConfigSerializer<T> serializer = serializerFactory.create(configDefinition, configClass);
		ConfigHolder<T> holder = new ConfigHolder<>(configDefinition, configClass, serializer);
		CONFIGS.put(Pair.of(configClass, configName), holder);
		return holder;
	}

	private static synchronized <T extends IConfigData> IConfigHolder<T> registerClient(Class<T> configClass,
			ResourceLocation configName, IConfigSerializer.Factory<T> serializerFactory) {
		Config configDefinition = configClass.getAnnotation(Config.class);
		IConfigSerializer<T> serializer = serializerFactory.create(configDefinition, configClass);
		ConfigHolder<T> holder = new ConfigHolder<>(configDefinition, configClass, serializer);
		CONFIGS.put(Pair.of(configClass, configName), holder);
		return holder;
	}

	private static synchronized boolean containsKey(Class<?> clazz) {
		for (Entry<Pair<Class<? extends IConfigData>, ResourceLocation>, IConfigHolder<?>> entry : CONFIGS.entrySet()) {
			if (entry.getKey().first == clazz) { return true; }
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	public static synchronized <T extends IConfigData> ConfigHolder<T> getConfigHolder(Class<T> configClass) {
		Objects.requireNonNull(configClass);
		for (Entry<Pair<Class<? extends IConfigData>, ResourceLocation>, IConfigHolder<?>> entry : CONFIGS.entrySet()) {
			if (entry.getKey().first == configClass) { return (ConfigHolder<T>) entry.getValue(); }
		}
		throw new RuntimeException(String.format("Config '%s' has not (yet) been registered", configClass));
	}

	@SuppressWarnings("unchecked")
	public static synchronized <T extends IConfigData> ConfigHolder<T> getConfigHolder(ResourceLocation name) {
		Objects.requireNonNull(name);
		for (Entry<Pair<Class<? extends IConfigData>, ResourceLocation>, IConfigHolder<?>> entry : CONFIGS.entrySet()) {
			if (entry.getKey().second.equals(name)) { return (ConfigHolder<T>) entry.getValue(); }
		}
		throw new RuntimeException(String.format("Config '%s' has not (yet) been registered", name));
	}

	public static synchronized <T extends IConfigData> ResourceLocation getConfigName(Class<T> configClass) {
		Objects.requireNonNull(configClass);
		for (Pair<Class<? extends IConfigData>, ResourceLocation> entry : CONFIGS.keySet()) {
			if (entry.first == configClass) { return entry.second; }
		}
		throw new RuntimeException(String.format("Config '%s' has not (yet) been registered", configClass));
	}

	public static synchronized <T extends IConfigData> ModConfig.Type getConfigType(Class<T> configClass) {
		return configClass.getAnnotation(Config.class).configType();
	}

	/*
	 * public static synchronized void syncServerValues(Class<? extends IConfigData>
	 * configClass) { IConfigHolder<?> holder = getConfigHolder(configClass); try {
	 * if (configClass.getAnnotation(Config.class).configType() == Type.SERVER) {
	 * BaseNetwork.sendToAll(MatyLibNetwork.CONFIG_CHANNEL, new
	 * S2CSyncServerConfigMessage( getConfigName(configClass),
	 * FileUtils.readFileToByteArray(holder.getConfigFile()))); } } catch
	 * (IOException e) { holder.getLogger().catching(e); } }
	 */

	// TODO proper server config and sync

	/*
	 * @SubscribeEvent(priority = EventPriority.HIGHEST) public static void
	 * handleServerAboutToStart(final FMLServerAboutToStartEvent server) { new
	 * Thread(() -> { CONFIGS.forEach((id, holder) -> { if
	 * (id.first.getAnnotation(Config.class).configType() == Type.SERVER) {
	 * holder.load(); syncServerValues(id.first); } }); },
	 * "Server config loader").start(); }
	 */

	@EventBusSubscriber(value = Dist.CLIENT, bus = Bus.MOD)
	public static class ClientEvents {

		@SubscribeEvent(priority = EventPriority.HIGHEST)
		public static void onConstructMod(final FMLConstructModEvent event) {
			new Thread(() -> {
				CONFIGS.forEach((id, holder) -> {
					if (id.first.getAnnotation(Config.class).configType() == Type.CLIENT) {
						holder.load();
						try {
							FileWatcher.defaultInstance().addWatch(holder.getConfigFile(), ConfigWatcher.of(holder));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
			}, "Client config loader").start();
		}

	}

	@EventBusSubscriber(bus = Bus.MOD)
	public static class CommonEvents {

		@SubscribeEvent(priority = EventPriority.HIGHEST)
		public static void onConstructMod(final FMLConstructModEvent event) {
			new Thread(() -> {
				CONFIGS.forEach((id, holder) -> {
					if (id.first.getAnnotation(Config.class).configType() == Type.COMMON) {
						holder.load();
						try {
							FileWatcher.defaultInstance().addWatch(holder.getConfigFile(), ConfigWatcher.of(holder));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				});
			}, "Common config loader").start();
		}

	}

	public static class ConfigWatcher<C extends IConfigData> implements Runnable {

		private final IConfigHolder<C> holder;

		private ConfigWatcher(IConfigHolder<C> holder) {
			this.holder = holder;
		}

		public static <C extends IConfigData> ConfigWatcher<C> of(IConfigHolder<C> holder) {
			return new ConfigWatcher<>(holder);
		}

		@Override
		public void run() {
			holder.load();
			holder.getLogger().info("Updated config file {}", holder.getConfigClass());
		}

	}
}
