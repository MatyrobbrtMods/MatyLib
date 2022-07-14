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

package com.matyrobbrt.lib.datagen.patchouli.type;

import java.util.ArrayList;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

/**
 * For information on what all the properties do, please visit the Patchouli
 * wiki: https://vazkiimods.github.io/Patchouli/
 * 
 * @author matyrobbrt
 *
 */
public class PatchouliBook {

	public String name;
	public String landingText;
	public ResourceLocation model;
	public String tab;
	public ResourceLocation texture;
	public ItemLike indexIcon;
	public boolean showProgress;
	public int version;

	public ArrayList<PatchouliMacro> macros = new ArrayList<>();

	public ResourceLocation craftingTexture;

	public String textColor;
	public String headerColor;
	public String nameplateColor;

	public PatchouliBook(String name, String landingText) {
		this.name = name;
		this.landingText = landingText;
	}

	public PatchouliBook setModel(ResourceLocation model) {
		this.model = model;
		return this;
	}

	public PatchouliBook setTab(String tab) {
		this.tab = tab;
		return this;
	}

	public PatchouliBook setBookTexture(ResourceLocation texture) {
		this.texture = texture;
		return this;
	}

	public PatchouliBook setIndexIcon(ItemLike indexIcon) {
		this.indexIcon = indexIcon;
		return this;
	}

	public PatchouliBook showProgress(boolean showProgress) {
		this.showProgress = showProgress;
		return this;
	}

	public PatchouliBook setVersion(int version) {
		this.version = version;
		return this;
	}

	public PatchouliBook addMacro(PatchouliMacro macro) {
		this.macros.add(macro);
		return this;
	}

	public PatchouliBook setCraftingTexture(ResourceLocation craftingTexture) {
		this.craftingTexture = craftingTexture;
		return this;
	}

	public PatchouliBook setTextColor(String textColor) {
		this.textColor = textColor;
		return this;
	}

	public PatchouliBook setHeaderColor(String headerColor) {
		this.headerColor = headerColor;
		return this;
	}

	public PatchouliBook setNameplateColor(String nameplateColor) {
		this.nameplateColor = nameplateColor;
		return this;
	}

	public PatchouliBook addDefaultMacros() {
		this.macros.add(new PatchouliMacro("<b>", "$(l)"));
		this.macros.add(new PatchouliMacro("<i>", "$(o)"));
		this.macros.add(new PatchouliMacro("</>", "$()"));
		this.macros.add(new PatchouliMacro("<br2>", "$(br2)"));
		this.macros.add(new PatchouliMacro("<item>", "$(item)"));
		this.macros.add(new PatchouliMacro("<player>", "$(playername)"));
		return this;
	}

	public JsonElement serialize() {
		JsonObject book = new JsonObject();

		addProperty(book, "name", this.name);
		addProperty(book, "landing_text", this.landingText);
		if (model != null)
			book.addProperty("model", this.model.toString());
		addProperty(book, "creative_tab", this.tab);
		if (model != null)
			book.addProperty("book_texture", this.texture.toString());
		if (indexIcon != null)
			book.addProperty("index_icon", Registry.ITEM.getKey(indexIcon.asItem()).toString());
		book.addProperty("show_progress", this.showProgress);
		book.addProperty("version", this.version);

		if (craftingTexture != null)
			book.addProperty("crafting_texture", craftingTexture.toString());

		addProperty(book, "text_color", this.textColor);
		addProperty(book, "header_color", this.headerColor);
		addProperty(book, "nameplate_color", this.nameplateColor);

		JsonObject macrosJson = new JsonObject();
		this.macros.forEach(macro -> macrosJson.addProperty(macro.key, macro.value));

		book.add("macros", macrosJson);

		return book;
	}

	private static void addProperty(JsonObject object, String property, String value) {
		if (value != null) {
			object.addProperty(property, value);
		}
	}

}
