package com.matyrobbrt.lib.datagen.recipe;

import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;

public class IngredientInput {

	private final Ingredient ingredient;
	private final int count;
	
	public IngredientInput(Ingredient ingredient, int count) {
		this.ingredient = ingredient;
		this.count = count;
	}
	
	public IngredientInput(Item item, int count) {
		this.ingredient = Ingredient.of(item);
		this.count = count;
	}
	
	public IngredientInput(ITag<Item> tag, int count) {
		this.ingredient = Ingredient.of(tag);
		this.count = count;
	}

	public Ingredient getIngredient() {
		return this.ingredient;
	}
	
	public int getCount() {
		return this.count;
	}
	
}
