package com.matyrobbrt.lib.datagen.patchouli.page;

import javax.annotation.Nonnull;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class LinkPage implements IPatchouliPage {
	
	public String url;
	public String linkText;
	
	public LinkPage(@Nonnull String url, @Nonnull String linkText) {
		this.url = url;
		this.linkText = linkText;
	}

	@Override
	public String getType() {
		return "patchouli:link";
	}

	@Override
	public JsonElement serialize() {
		JsonObject object = new JsonObject();
		object.addProperty("type", getType());
		object.addProperty("url", this.url);
		object.addProperty("link_text", this.linkText);
		return object;
	}

}
