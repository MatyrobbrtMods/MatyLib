package com.matyrobbrt.lib.datagen.patchouli.page;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SmeltingRecipePage extends CraftingRecipePage {

	public SmeltingRecipePage(String recipe) {
		super(recipe);
	}

	@Override
	public String getType() {
		return "patchouli:smleting";
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
