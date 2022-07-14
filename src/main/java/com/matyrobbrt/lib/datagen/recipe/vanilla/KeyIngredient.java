/**
 * This file is part of the MatyLib Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2022 Matyrobbrt
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.matyrobbrt.lib.datagen.recipe.vanilla;

import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;

import net.minecraftforge.registries.RegistryObject;

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
	public KeyIngredient(Character key, net.minecraft.tags.Tag<Item> tag) {
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
