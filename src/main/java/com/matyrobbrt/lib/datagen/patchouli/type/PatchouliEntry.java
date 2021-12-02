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

package com.matyrobbrt.lib.datagen.patchouli.type;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.matyrobbrt.lib.datagen.patchouli.page.IPatchouliPage;

import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;

public class PatchouliEntry {

	public String category;
	public String fileName;
	public String displayName;
	public Item icon;

	public String turnin;
	
	public ArrayList<IPatchouliPage> pages = new ArrayList<>();

	public PatchouliEntry(String category, String displayName, IItemProvider icon) {
		this.category = category;
		this.fileName = displayName.toLowerCase().replace(' ', '_');
		this.displayName = displayName;
		this.icon = icon.asItem();
	}
	
	public PatchouliEntry(PatchouliCategory category, String displayName, IItemProvider icon) {
		this.category = category.fileName;
		this.fileName = displayName.toLowerCase().replace(' ', '_');
		this.displayName = displayName;
		this.icon = icon.asItem();
	}

	public PatchouliEntry addPage(IPatchouliPage page) {
		this.pages.add(page);
		return this;
	}
	
	public PatchouliEntry setTurnin(String turnin) {
		this.turnin = turnin;
		return this;
	}

	public JsonObject serialize() {
		JsonObject object = new JsonObject();

		object.addProperty("name", this.displayName);
		object.addProperty("category", this.category);

		if (this.icon != null)
			object.addProperty("icon", this.icon.getRegistryName().toString());

		JsonArray pagesElement = new JsonArray();
		this.pages.forEach(page -> pagesElement.add(page.serialize()));
		object.add("pages", pagesElement);
		
		addProperty(object, "turnin", this.turnin);
		
		return object;
	}
	
	private static void addProperty(JsonObject object, String property, String value) {
		if (value != null) {
			object.addProperty(property, value);
		}
	}

}
