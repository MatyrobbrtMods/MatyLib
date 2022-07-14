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

package com.matyrobbrt.lib.registry;

import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class BetterRegistryObject<T extends IForgeRegistryEntry<?>> implements Supplier<T> {

	private final Supplier<T> sup;
	private ResourceLocation name;
	private final IForgeRegistry<? super T> registry;

	public BetterRegistryObject(Supplier<T> sup, IForgeRegistry<? super T> registry) {
		this.sup = sup;
		this.registry = registry;
	}

	public static <F extends Fluid> BetterRegistryObject<F> fluid(Supplier<F> sup) {
		return new BetterRegistryObject<>(sup, ForgeRegistries.FLUIDS);
	}

	@Override
	@SuppressWarnings("unchecked")
	public T get() {
		return (T) registry.getValue(name);
	}

	@SuppressWarnings("unused")
	private Supplier<T> getDeclaredValue() { return sup; }

	@SuppressWarnings("unused")
	private void setName(ResourceLocation name) { this.name = name; }

}
