package com.matyrobbrt.lib.datagen.recipe.vanilla;

import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ITag;

import net.minecraftforge.fml.RegistryObject;

/**
 * Class for storing key ingredients for Vanilla Crafting Recipes
 * @author matyrobbrt
 *
 */
public class KeyIngredient {

	private final Character key;
	private final Ingredient ingredient;

	/**
	 * Makes a new ingredient with an attached key for Vanilla Crafting recipes
	 * @param key the key of the ingredient
	 * @param item the item that will be the ingredient
	 */
	public KeyIngredient(Character key, Item item) {
		this.key = key;
		this.ingredient = Ingredient.of(item);
	}
	
	/**
	 * Makes a new ingredient with an attached key for Vanilla Crafting recipes (see: {@link #KeyIngredient})
	 * @param key the key of the ingredient
	 * @param ingredient the ingredient (Ingredient.of(ITEM))
	 */
	public KeyIngredient(Character key, Ingredient ingredient) {
		this.key = key;
		this.ingredient = ingredient;
	}
	
	/**
	 * Makes a new ingredient with an attached key for Vanilla Crafting recipes (see: {@link #KeyIngredient})
	 * @param key the key of the ingredient
	 * @param item the item (this method ONLY accepts RegistryObjects)
	 */
	public KeyIngredient(Character key, RegistryObject<Item> item) {
		this.key = key;
		this.ingredient = Ingredient.of(item.get());
	}

	/**
	 * Makes a new ingredient with an attached key for Vanilla Crafting recipes (see: {@link #KeyIngredient})
	 * @param key the key of the ingredient
	 * @param tag the tag of the ingredient
	 */
	public KeyIngredient(Character key, ITag<Item> tag) {
		this.key = key;
		this.ingredient = Ingredient.of(tag);
	}

	/**
	 * Gets the key of the recipe
	 * @return the recipe key
	 */
	public Character getKey() {
		return this.key;
	}

	/**
	 * Gets the ingredient of the recipe
	 * @return the recipe ingredient
	 */
	public Ingredient getIngredient() {
		return this.ingredient;
	}

	/**
	 * Inserts the ingredient in a {@link ShapedRecipeBuilder}
	 * @param recipe the recipe to insert the ingredient in
	 */
	public void defineShapedRecipe(ShapedRecipeBuilder recipe) {
		recipe.define(this.key, this.ingredient);
	}

}
