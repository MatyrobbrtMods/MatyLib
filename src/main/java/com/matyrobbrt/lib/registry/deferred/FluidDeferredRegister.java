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

package com.matyrobbrt.lib.registry.deferred;

import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidDeferredRegister extends DeferredRegisterWrapper<Fluid> {

	protected final DeferredRegister<Block> blocks;
	protected final DeferredRegister<Item> items;

	protected FluidDeferredRegister(String modID) {
		super(ForgeRegistries.FLUIDS, modID);
		blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, modID);
		items = DeferredRegister.create(ForgeRegistries.ITEMS, modID);
	}

	@Override
	public void register(IEventBus modBus) {
		super.register(modBus);
		blocks.register(modBus);
		items.register(modBus);
	}

	public <T extends Item> RegistryObject<T> registerItem(String name, Supplier<? extends T> sup) {
		return items.register(name, sup);
	}

	public <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<? extends T> sup) {
		return blocks.register(name, sup);
	}

	public <T extends Fluid> RegistryObject<T> registerFluid(String name, Supplier<? extends T> sup) {
		return register.register(name, sup);
	}
}
