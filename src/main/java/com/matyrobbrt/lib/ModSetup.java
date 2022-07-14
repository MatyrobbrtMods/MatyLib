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

package com.matyrobbrt.lib;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * Base class for mod main classes
 *
 * @author matyrobbrt
 *
 */
public abstract class ModSetup {

	public final IEventBus modBus;
	public final IEventBus forgeBus;

	protected final String modId;

	/**
	 * @param modid needed to that classes that inherit this one <b>have to</b> call
	 *              super
	 */
	protected ModSetup(String modid) {
		modId = modid;
		modBus = FMLJavaModLoadingContext.get().getModEventBus();
		modBus.addListener(this::onCommonSetup);
		modBus.addListener(this::onInterModEnqueue);

		clientSetup().ifPresent(sup -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> sup::get));

		forgeBus = MinecraftForge.EVENT_BUS;
		forgeBus.register(this);
	}

	public ResourceLocation rl(String name) {
		return new ResourceLocation(modId, name);
	}

	public Optional<Supplier<ClientSetup>> clientSetup() {
		return Optional.empty();
	}

	public void onCommonSetup(final FMLCommonSetupEvent event) {
	}

	public void onInterModEnqueue(final InterModEnqueueEvent event) {

	}

	public static boolean isProduction() { return FMLEnvironment.production; }
}
