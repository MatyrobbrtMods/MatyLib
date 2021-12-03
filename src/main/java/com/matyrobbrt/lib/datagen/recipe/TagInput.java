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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;

/**
 * Class for defining tag inputs of recipe
 * @author matyrobbrt
 *
 */
public class TagInput implements IIngredient {
	
	private final ITag<Item> tag;
	private final int count;

	/**
	 * Creates a new recipe tag input
	 * @param tag the tag of the input
	 * @param count the count of the input
	 */
	public TagInput(ITag<Item> tag, int count) {
		this.tag = tag;
		this.count = count;
	}

	/**
	 * Gets the tag of the input
	 * @return the tag of the input
	 */
	public ITag<Item> getTag() {
		return this.tag;
	}

	/**
	 * Gets the count of the input
	 * @return the count of the input
	 */
	public int getCount() {
		return this.count;
	}

	@Override
	public JsonElement toJson() {
		JsonObject obj = new JsonObject();
		obj.addProperty("count", count);
		obj.addProperty("tag", tag.toString().replace("[", "").replace("]", "").replace("NamedTag", ""));
		return obj;
	}
}
