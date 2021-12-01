package com.matyrobbrt.lib.datagen.patchouli.type;

import java.util.ArrayList;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

public class PatchouliBook {

	public String name;
	public String landingText;
	public ResourceLocation model;
	public String tab;
	public ResourceLocation texture;
	public IItemProvider indexIcon;
	public boolean showProgress;
	public int version;

	public ArrayList<PatchouliMacro> macros = new ArrayList<>();
	
	public ResourceLocation craftingTexture;
	
	public String textColor;
	public String headerColor;
	public String nameplateColor;

	public PatchouliBook(String name, String landingText) {
		this.name = name;
		this.landingText = landingText;
	}

	public PatchouliBook setModel(ResourceLocation model) {
		this.model = model;
		return this;
	}

	public PatchouliBook setTab(String tab) {
		this.tab = tab;
		return this;
	}

	public PatchouliBook setBookTexture(ResourceLocation texture) {
		this.texture = texture;
		return this;
	}

	public PatchouliBook setIndexIcon(IItemProvider indexIcon) {
		this.indexIcon = indexIcon;
		return this;
	}

	public PatchouliBook showProgress(boolean showProgress) {
		this.showProgress = showProgress;
		return this;
	}

	public PatchouliBook setVersion(int version) {
		this.version = version;
		return this;
	}

	public PatchouliBook addMacro(PatchouliMacro macro) {
		this.macros.add(macro);
		return this;
	}
	
	public PatchouliBook setCraftingTexture(ResourceLocation craftingTexture) {
		this.craftingTexture = craftingTexture;
		return this;
	}
	
	public PatchouliBook setTextColor(String textColor) {
		this.textColor = textColor;
		return this;
	}
	
	public PatchouliBook setHeaderColor(String headerColor) {
		this.headerColor = headerColor;
		return this;
	}
	
	public PatchouliBook setNameplateColor(String nameplateColor) {
		this.nameplateColor = nameplateColor;
		return this;
	}
	
	public PatchouliBook addDefaultMacros() {
		this.macros.add(new PatchouliMacro("<b>", "$(l)"));
		this.macros.add(new PatchouliMacro("<i>", "$(o)"));
		this.macros.add(new PatchouliMacro("</>", "$()"));
		this.macros.add(new PatchouliMacro("<br2>", "$(br2)"));
		this.macros.add(new PatchouliMacro("<item>", "$(item)"));
		this.macros.add(new PatchouliMacro("<player>", "$(playername)"));
		return this;
	}

	public JsonElement serialize() {
		JsonObject book = new JsonObject();

		addProperty(book, "name", this.name);
		addProperty(book, "landing_text", this.landingText);
		addProperty(book, "model", this.model.toString());
		addProperty(book, "creative_tab", this.tab);
		addProperty(book, "book_texture", this.texture.toString());
		addProperty(book, "index_icon", this.indexIcon.asItem().getRegistryName().toString());
		book.addProperty("show_progress", this.showProgress);
		book.addProperty("version", this.version);
		
		addProperty(book, "crafting_texture", this.craftingTexture.toString());
		
		addProperty(book, "text_color", this.textColor);
		addProperty(book, "header_color", this.headerColor);
		addProperty(book, "nameplate_color", this.nameplateColor);

		JsonObject macrosJson = new JsonObject();
		this.macros.forEach(macro -> macrosJson.addProperty(macro.key, macro.value));
		
		book.add("macros", macrosJson);

		return book;
	}

	private void addProperty(JsonObject object, String property, String value) {
		if (value != null)
			object.addProperty(property, value);
	}

}
