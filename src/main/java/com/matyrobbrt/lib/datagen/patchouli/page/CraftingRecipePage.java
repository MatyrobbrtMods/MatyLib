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
