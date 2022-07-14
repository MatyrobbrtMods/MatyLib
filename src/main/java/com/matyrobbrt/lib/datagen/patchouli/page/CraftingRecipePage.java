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

package com.matyrobbrt.lib.datagen.patchouli.page;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class CraftingRecipePage implements IPatchouliPage {

	public String recipe;
	public String recipe2;
	public String text;
	public String title;

	public CraftingRecipePage(String recipe) {
		this.recipe = recipe;
	}

	public CraftingRecipePage addSecondRecipe(String recipe2) {
		this.recipe2 = recipe2;
		return this;
	}

	public CraftingRecipePage setTitle(String title) {
		this.title = title;
		return this;
	}

	public CraftingRecipePage setText(String text) {
		this.text = text;
		return this;
	}

	@Override
	public String getType() {
		return "patchouli:crafting";
	}

	@Override
	public JsonElement serialize() {
		JsonObject object = new JsonObject();
		object.addProperty("type", getType());
		object.addProperty("recipe", this.recipe);
		if (this.recipe2 != null)
			object.addProperty("recipe2", this.recipe2);
		if (this.title != null)
			object.addProperty("title", this.title);
		if (this.text != null)
			object.addProperty("text", this.text);
		return object;
	}

}
