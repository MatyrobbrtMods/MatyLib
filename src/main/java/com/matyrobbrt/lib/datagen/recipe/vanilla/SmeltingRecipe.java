/**
 * This file is part of the MatyLib Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2021 Matyrobbrt
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

import com.matyrobbrt.lib.util.helper.DataGenHelper;

import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;

/**
 * This class is used for creating Vanilla Smelting recipes
 * 
 * @author matyrobbrt
 *
 */
public class SmeltingRecipe {

	protected ItemLike output;
	protected Ingredient ingredient;

	public ItemLike getOutput() { return output; }

	public Ingredient getIngredient() { return ingredient; }

	/**
	 * Smelting Recipe
	 * 
	 * @author matyrobbrt
	 *
	 */
	public static class Smelting extends SmeltingRecipe {

		private final Float experience;
		private final int cookingTime;

		/**
		 * Makes a new Smelting recipe, with 0.7 experience and 200 ticks cook time
		 * 
		 * @param ingredient the ingredient of the recipe (Ingredient.of(ITEM))
		 * @param output     the output of the recipe
		 */
		public Smelting(Ingredient ingredient, ItemLike output) {
			this.ingredient = ingredient;
			this.output = output;
			this.experience = 0.7f;
			this.cookingTime = 200;
		}

		/**
		 * Makes a new Smelting recipe
		 * 
		 * @param ingredient  the ingredient of the recipe (Ingredient.of(ITEM))
		 * @param output      the output of the recipe
		 * @param experience  the amount of experience that the recipe will produce
		 * @param cookingTime the time in ticks the cooking recipe will take
		 */
		public Smelting(Ingredient ingredient, ItemLike output, Float experience, int cookingTime) {
			this.ingredient = ingredient;
			this.output = output;
			this.experience = experience;
			this.cookingTime = cookingTime;
		}

		public SimpleCookingRecipeBuilder getRecipe() {
			return SimpleCookingRecipeBuilder.smelting(ingredient, output, experience, cookingTime)
					.unlockedBy("has_item", DataGenHelper.Criterion.has(ingredient));
		}
	}

	/**
	 * Blasting Recipe
	 * 
	 * @author matyrobbrt
	 *
	 */
	public static class Blasting extends SmeltingRecipe {

		private final Float experience;
		private final int cookingTime;

		/**
		 * Makes a new Blasting recipe, with 0.7 experience and 100 ticks cook time
		 * 
		 * @param ingredient the ingredient of the recipe (Ingredient.of(ITEM))
		 * @param output     the output of the recipe
		 */
		public Blasting(Ingredient ingredient, ItemLike output) {
			this.ingredient = ingredient;
			this.output = output;
			this.experience = 0.7f;
			this.cookingTime = 100;
		}

		/**
		 * Makes a new Blasting recipe
		 * 
		 * @param ingredient  the ingredient of the recipe (Ingredient.of(ITEM))
		 * @param output      the output of the recipe
		 * @param experience  the amount of experience that the recipe will produce
		 * @param cookingTime the time in ticks the cooking recipe will take
		 */
		public Blasting(Ingredient ingredient, ItemLike output, Float experience, int cookingTime) {
			this.ingredient = ingredient;
			this.output = output;
			this.experience = experience;
			this.cookingTime = cookingTime;
		}

		public SimpleCookingRecipeBuilder getRecipe() {
			return SimpleCookingRecipeBuilder.blasting(ingredient, output, experience, cookingTime)
					.unlockedBy("has_item", DataGenHelper.Criterion.has(ingredient));
		}

	}

	/**
	 * Cooking Recipes
	 * 
	 * @author matyrobbrt
	 *
	 */
	public static class Cooking {

		/**
		 * New Smoking Recipe
		 * 
		 * @author matyrobbrt
		 *
		 */
		public static class Smoking extends SmeltingRecipe {

			private final Float experience;
			private final int cookingTime;

			/**
			 * Makes a new Smoking recipe, with 0.35 experience and 100 ticks cook time
			 * 
			 * @param ingredient the ingredient of the recipe (Ingredient.of(ITEM))
			 * @param output     the output of the recipe
			 */
			public Smoking(Ingredient ingredient, ItemLike output) {
				this.ingredient = ingredient;
				this.output = output;
				this.experience = 0.35f;
				this.cookingTime = 100;
			}

			/**
			 * Makes a new Smoking recipe
			 * 
			 * @param ingredient  the ingredient of the recipe (Ingredient.of(ITEM))
			 * @param output      the output of the recipe
			 * @param experience  the amount of experience that the recipe will produce
			 * @param cookingTime the time in ticks the cooking recipe will take
			 */
			public Smoking(Ingredient ingredient, ItemLike output, Float experience, int cookingTime) {
				this.ingredient = ingredient;
				this.output = output;
				this.experience = experience;
				this.cookingTime = cookingTime;
			}

			public SimpleCookingRecipeBuilder getRecipe() {
				return SimpleCookingRecipeBuilder
						.cooking(ingredient, output, experience, cookingTime, RecipeSerializer.SMOKING_RECIPE)
						.unlockedBy("has_item", DataGenHelper.Criterion.has(ingredient));
			}

		}

		/**
		 * New Campfire Cooking Recipe
		 * 
		 * @author matyrobbrt
		 *
		 */
		public static class CampfireCooking extends SmeltingRecipe {

			private final Float experience;
			private final int cookingTime;

			/**
			 * Makes a new Campfire Cooking recipe, with 0.35 experience and 600 ticks cook
			 * time
			 * 
			 * @param ingredient the ingredient of the recipe (Ingredient.of(ITEM))
			 * @param output     the output of the recipe
			 */
			public CampfireCooking(Ingredient ingredient, ItemLike output) {
				this.ingredient = ingredient;
				this.output = output;
				this.experience = 0.35f;
				this.cookingTime = 600;
			}

			/**
			 * Makes a new Campfire Cooking recipe
			 * 
			 * @param ingredient  the ingredient of the recipe (Ingredient.of(ITEM))
			 * @param output      the output of the recipe
			 * @param experience  the amount of experience that the recipe will produce
			 * @param cookingTime the time in ticks the cooking recipe will take
			 */
			public CampfireCooking(Ingredient ingredient, ItemLike output, Float experience, int cookingTime) {
				this.ingredient = ingredient;
				this.output = output;
				this.experience = experience;
				this.cookingTime = cookingTime;
			}

			public SimpleCookingRecipeBuilder getRecipe() {
				return SimpleCookingRecipeBuilder
						.cooking(ingredient, output, experience, cookingTime, RecipeSerializer.CAMPFIRE_COOKING_RECIPE)
						.unlockedBy("has_item", DataGenHelper.Criterion.has(ingredient));
			}

		}
	}

}
