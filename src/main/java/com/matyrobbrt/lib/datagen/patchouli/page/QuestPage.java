package com.matyrobbrt.lib.datagen.patchouli.page;

import javax.annotation.Nullable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class QuestPage implements IPatchouliPage {
	
	public String trigger;
	public String text;
	public String title;
	
	public QuestPage(@Nullable String trigger, @Nullable String text, @Nullable String title) {
		this.trigger = trigger;
		this.text = text;
		this.title = title;
	}

	@Override
	public String getType() {
		return "patchouli:quest";
	}

	@Override
	public JsonElement serialize() {
		JsonObject object = new JsonObject();
		addType(object);
		addProperty(object, "trigger", this.trigger);
		addProperty(object, "title", this.title);
		addProperty(object, "text", this.text);
		return object;
	}

}
