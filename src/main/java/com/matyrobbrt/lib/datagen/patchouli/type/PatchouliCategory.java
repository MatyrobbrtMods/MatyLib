package com.matyrobbrt.lib.datagen.patchouli.type;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.matyrobbrt.lib.datagen.patchouli.vars.StringItemStack;

import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

public class PatchouliCategory {

	public String name;
	public String fileName;
	public String description;
	public IItemProvider icon;
	
	public ResourceLocation textureIcon;
	
	public StringItemStack itemstackIcon;

	public PatchouliCategory(String name, String fileName, String description, @Nullable IItemProvider icon) {
		this.name = name;
		this.fileName = fileName;
		this.description = description;
		this.icon = icon;
	}
	
	public PatchouliCategory setIcon(ResourceLocation textureIcon) {
		this.icon = null;
		this.textureIcon = textureIcon;
		this.itemstackIcon = null;
		return this;
	}
	
	public PatchouliCategory setIcon(StringItemStack itemstackIcon) {
		this.icon = null;
		this.textureIcon = null;
		this.itemstackIcon = itemstackIcon;
		return this;
	}

	public JsonElement serialize() {
		JsonObject object = new JsonObject();

		object.addProperty("name", this.name);
		object.addProperty("description", this.description);
		
		if (this.icon != null)
			object.addProperty("icon", this.icon.asItem().getRegistryName().toString());
		
		if (this.textureIcon != null)
			object.addProperty("icon", this.textureIcon.toString());
		
		if (this.itemstackIcon != null)
			object.addProperty("icon", this.itemstackIcon.toString());

		return object;
	}

}
