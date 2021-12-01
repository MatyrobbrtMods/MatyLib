package com.matyrobbrt.lib.datagen.patchouli.page;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class RelationsPage implements IPatchouliPage {
	
	public String[] entries;
	public String title;
	public String text;
	
	public RelationsPage(@Nonnull String[] entries, @Nullable String title, @Nullable String text) {
		this.entries = entries;
		this.title = title;
		this.text = text;
	}

	@Override
	public String getType() {
		return "patchouli:relations";
	}

	@Override
	public JsonElement serialize() {
		JsonObject object = new JsonObject();
		object.addProperty("type", getType());
		JsonArray entriesJson = new JsonArray();
		for (int i = 0; i <= this.entries.length - 1; i++) {
			entriesJson.add(this.entries[i]);
		}
		object.add("entries", entriesJson);
		if (this.title != null)
			object.addProperty("title", this.title);
		if (this.text != null)
			object.addProperty("text", this.text);
		return object;
	}

}
