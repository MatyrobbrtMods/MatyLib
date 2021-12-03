package com.matyrobbrt.lib.datagen.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.util.IItemProvider;

public class ItemInput implements IIngredient {

	public final IItemProvider item;
	public final int count;
	
	public ItemInput(IItemProvider item, int count) {
		this.item = item;
		this.count = count;
	}

	@Override
	public JsonElement toJson() {
		JsonObject obj = new JsonObject();
		obj.addProperty("item", item.asItem().getRegistryName().toString());
		obj.addProperty("count", count);
		return obj;
	}
	
}
