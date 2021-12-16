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

package com.matyrobbrt.lib.datagen.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Class for defining outputs of recipes
 * @author matyrobbrt
 *
 */
public class Output {

	private final Item item;
	private final int count;
	
	/**
	 * Creates a new recipe output
	 * @param item the item output
	 * @param count the count of the output
	 */
	public Output(Item item, int count) {
		this.item = item;
		this.count = count;
	}
	
	/**
	 * @see #Output
	 */
	public Output(RegistryObject<Item> item, int count) {
		this.item = item.get();
		this.count = count;
	}
	
	/**
	 * @see #Output
	 */
	public Output(ResourceLocation item, int count) {
		this.item = ForgeRegistries.ITEMS.getValue(item);
		this.count = count;
	}
	
	/**
	 * Gets the output item
	 * @return the output item
	 */
	public Item getItem() {
		return this.item;
	}
	
	/**
	 * Gets the output count
	 * @return the output count
	 */
	public int getCount() {
		return this.count;
	}
	
}
