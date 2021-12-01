package com.matyrobbrt.lib.datagen.patchouli.page;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TextPage implements IPatchouliPage {

	public String text;
	public String title;

	public TextPage(@Nullable String title, @Nonnull String text) {
		this.text = text;
		this.title = title;
	}

	@Override
	public String getType() {
		return "patchouli:text";
	}

	@Override
	public JsonElement serialize() {
		JsonObject object = new JsonObject();
		object.addProperty("type", getType());
		object.addProperty("text", this.text);
		if (this.title != null)
			object.addProperty("title", this.title);
		return object;
	}

}
