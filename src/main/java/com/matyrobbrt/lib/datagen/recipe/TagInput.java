package com.matyrobbrt.lib.datagen.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;

/**
 * Class for defining tag inputs of recipe
 * @author matyrobbrt
 *
 */
public class TagInput implements IIngredient {
	
	private final ITag<Item> tag;
	private final int count;

	/**
	 * Creates a new recipe tag input
	 * @param tag the tag of the input
	 * @param count the count of the input
	 */
	public TagInput(ITag<Item> tag, int count) {
		this.tag = tag;
		this.count = count;
	}

	/**
	 * Gets the tag of the input
	 * @return the tag of the input
	 */
	public ITag<Item> getTag() {
		return this.tag;
	}

	/**
	 * Gets the count of the input
	 * @return the count of the input
	 */
	public int getCount() {
		return this.count;
	}

	@Override
	public JsonElement toJson() {
		JsonObject obj = new JsonObject();
		obj.addProperty("count", count);
		obj.addProperty("tag", tag.toString().replace("[", "").replace("]", "").replace("NamedTag", ""));
		return obj;
	}
}
