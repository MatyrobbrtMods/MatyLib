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

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.matyrobbrt.lib.datagen.patchouli.vars.StringItemStack;

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
public class PatchouliCategory {

	public String name;
	public String fileName;
	public String description;
	public ItemLike icon;

	public ResourceLocation textureIcon;

	public StringItemStack itemstackIcon;

	public PatchouliCategory(String name, String fileName, String description, @Nullable ItemLike icon) {
		this.name = name;
		this.fileName = fileName;
		this.description = description;
		this.icon = icon;
	}

	public PatchouliCategory setIcon(ResourceLocation textureIcon) {
		icon = null;
		this.textureIcon = textureIcon;
		itemstackIcon = null;
		return this;
	}

	public PatchouliCategory setIcon(StringItemStack itemstackIcon) {
		icon = null;
		textureIcon = null;
		this.itemstackIcon = itemstackIcon;
		return this;
	}

	public JsonElement serialize() {
		JsonObject object = new JsonObject();

		object.addProperty("name", name);
		object.addProperty("description", description);

		if (icon != null) {
			object.addProperty("icon", Registry.ITEM.getKey(icon.asItem()).toString());
		}

		if (textureIcon != null) {
			object.addProperty("icon", textureIcon.toString());
		}

		if (itemstackIcon != null) {
			object.addProperty("icon", itemstackIcon.toString());
		}
		return object;
	}

}
