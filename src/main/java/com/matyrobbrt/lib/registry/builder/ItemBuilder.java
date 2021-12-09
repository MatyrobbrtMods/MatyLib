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

package com.matyrobbrt.lib.registry.builder;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.Function;
import java.util.function.Supplier;

import com.google.common.collect.Maps;

import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Rarity;

import net.minecraftforge.common.ToolType;

public class ItemBuilder<T extends Item> extends AbstractBuilder<T> {

	private final Function<Item.Properties, T> factory;

	private ItemGroup tab;
	private int stackSize;
	private int defaultDurability;
	private Food food;
	private Item craftRemainder;
	private Rarity rarity;
	private boolean fireResistant;
	private boolean noRepair = true;

	private Supplier<Callable<ItemStackTileEntityRenderer>> ister;
	private Map<ToolType, Integer> toolClasses = Maps.newHashMap();

	public ItemBuilder(Function<Properties, T> factory) {
		this.factory = factory;
	}

	public static ItemBuilder<Item> normal() {
		return new ItemBuilder<>(Item::new);
	}

	public ItemBuilder<T> tab(ItemGroup tab) {
		this.tab = tab;
		return this;
	}

	public ItemBuilder<T> stacksTo(int stackSize) {
		this.stackSize = stackSize;
		return this;
	}

	public ItemBuilder<T> defaultDurability(int defaultDurability) {
		this.defaultDurability = defaultDurability;
		return this;
	}

	public ItemBuilder<T> food(Food food) {
		this.food = food;
		return this;
	}

	public ItemBuilder<T> craftRemainder(Item craftRemainder) {
		this.craftRemainder = craftRemainder;
		return this;
	}

	public ItemBuilder<T> rarity(Rarity rarity) {
		this.rarity = rarity;
		return this;
	}

	public ItemBuilder<T> fireResistant() {
		this.fireResistant = true;
		return this;
	}

	public ItemBuilder<T> setNoRepair() {
		this.noRepair = false;
		return this;
	}

	public ItemBuilder<T> setISTER(Supplier<Callable<ItemStackTileEntityRenderer>> ister) {
		this.ister = ister;
		return this;
	}

	public ItemBuilder<T> addToolType(ToolType type, int level) {
		toolClasses.put(type, level);
		return this;
	}

	@Override
	public T build() {
		return factory.apply(properties());
	}

	public Properties properties() {
		Properties properties = new Properties();
		if (tab != null) {
			properties.tab(tab);
		}
		if (stackSize != 0) {
			properties.stacksTo(stackSize);
		}
		if (defaultDurability != 0) {
			properties.defaultDurability(defaultDurability);
		}
		if (food != null) {
			properties.food(food);
		}
		if (craftRemainder != null) {
			properties.craftRemainder(craftRemainder);
		}
		if (rarity != null) {
			properties.rarity(rarity);
		}
		if (fireResistant) {
			properties.fireResistant();
		}
		if (!noRepair) {
			properties.setNoRepair();
		}
		if (ister != null) {
			properties.setISTER(ister);
		}
		toolClasses.forEach(properties::addToolType);
		return properties;
	}

}
