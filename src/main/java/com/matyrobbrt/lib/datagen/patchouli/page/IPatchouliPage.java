package com.matyrobbrt.lib.datagen.patchouli.page;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public interface IPatchouliPage {
	
	String getType();
	
	JsonElement serialize();
	
	default void addType(JsonObject object) {
		object.addProperty("type", getType());
	}
	
	default void addProperty(JsonObject object, String property, String value) {
		if (value != null)
			object.addProperty(property, value);
	}

}
