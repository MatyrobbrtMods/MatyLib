package com.matyrobbrt.lib.datagen.patchouli.page;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.item.Item;
import net.minecraft.util.IItemProvider;

public class SpotlightPage implements IPatchouliPage {
	
	public String text;
	public Item item;
	
	public SpotlightPage(IItemProvider item, String text) {
		this.item = item.asItem();
		this.text = text;
	}

	@Override
	public String getType() {
		return "patchouli:spotlight";
	}

	@Override
	public JsonElement serialize() {
		JsonObject object = new JsonObject();
		
		object.addProperty("type", getType());
		object.addProperty("item", this.item.getRegistryName().toString());
		object.addProperty("text", this.text);
		
		return object;
	}

}
