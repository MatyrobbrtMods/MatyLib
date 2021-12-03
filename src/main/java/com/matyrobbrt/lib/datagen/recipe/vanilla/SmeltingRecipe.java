package com.matyrobbrt.lib.datagen.recipe.vanilla;

import com.matyrobbrt.lib.util.helper.DataGenHelper;

import net.minecraft.data.CookingRecipeBuilder;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;

/**
 * This class is used for creating Vanilla Smelting recipes
 * @author matyrobbrt
 *
 */
public class SmeltingRecipe {
	
	protected IItemProvider output;
	protected Ingredient ingredient;
	
	public IItemProvider getOutput() {
		return output;
	}
	
	public Ingredient getIngredient() {
		return ingredient;
	}

	/**
	 * Smelting Recipe
	 * @author matyrobbrt
	 *
	 */
	public static class Smelting extends SmeltingRecipe {

		private final Float experience;
		private final int cookingTime;

		/**
		 * Makes a new Smelting recipe, with 0.7 experience and 200 ticks cook time
		 * @param ingredient the ingredient of the recipe (Ingredient.of(ITEM))
		 * @param output the output of the recipe
		 */
		public Smelting(Ingredient ingredient, IItemProvider output) {
			this.ingredient = ingredient;
			this.output = output;
			this.experience = 0.7f;
			this.cookingTime = 200;
		}

		/**
		 * Makes a new Smelting recipe
		 * @param ingredient the ingredient of the recipe (Ingredient.of(ITEM))
		 * @param output the output of the recipe
		 * @param experience the amount of experience that the recipe will produce
		 * @param cookingTime the time in ticks the cooking recipe will take
		 */
		public Smelting(Ingredient ingredient, IItemProvider output, Float experience, int cookingTime) {
			this.ingredient = ingredient;
			this.output = output;
			this.experience = experience;
			this.cookingTime = cookingTime;
		}

		public CookingRecipeBuilder getRecipe() {
			return CookingRecipeBuilder.smelting(ingredient, output, experience, cookingTime).unlockedBy("has_item",
					DataGenHelper.Criterion.has(ingredient));
		}
	}

	/**
	 * Blasting Recipe
	 * @author matyrobbrt
	 *
	 */
	public static class Blasting extends SmeltingRecipe {

		private final Float experience;
		private final int cookingTime;

		/**
		 * Makes a new Blasting recipe, with 0.7 experience and 100 ticks cook time
		 * @param ingredient the ingredient of the recipe (Ingredient.of(ITEM))
		 * @param output the output of the recipe
		 */
		public Blasting(Ingredient ingredient, IItemProvider output) {
			this.ingredient = ingredient;
			this.output = output;
			this.experience = 0.7f;
			this.cookingTime = 100;
		}

		/**
		 * Makes a new Blasting recipe
		 * @param ingredient the ingredient of the recipe (Ingredient.of(ITEM))
		 * @param output the output of the recipe
		 * @param experience the amount of experience that the recipe will produce
		 * @param cookingTime the time in ticks the cooking recipe will take
		 */
		public Blasting(Ingredient ingredient, IItemProvider output, Float experience, int cookingTime) {
			this.ingredient = ingredient;
			this.output = output;
			this.experience = experience;
			this.cookingTime = cookingTime;
		}

		public CookingRecipeBuilder getRecipe() {
			return CookingRecipeBuilder.blasting(ingredient, output, experience, cookingTime).unlockedBy("has_item",
					DataGenHelper.Criterion.has(ingredient));
		}

	}

	/**
	 * Cooking Recipes
	 * @author matyrobbrt
	 *
	 */
	public static class Cooking {

		/**
		 * New Smoking Recipe
		 * @author matyrobbrt
		 *
		 */
		public static class Smoking extends SmeltingRecipe {

			private final Float experience;
			private final int cookingTime;

			/**
			 * Makes a new Smoking recipe, with 0.35 experience and 100 ticks cook time
			 * @param ingredient the ingredient of the recipe (Ingredient.of(ITEM))
			 * @param output the output of the recipe
			 */
			public Smoking(Ingredient ingredient, IItemProvider output) {
				this.ingredient = ingredient;
				this.output = output;
				this.experience = 0.35f;
				this.cookingTime = 100;
			}

			/**
			 * Makes a new Smoking recipe
			 * @param ingredient the ingredient of the recipe (Ingredient.of(ITEM))
			 * @param output the output of the recipe
			 * @param experience the amount of experience that the recipe will produce
			 * @param cookingTime the time in ticks the cooking recipe will take
			 */
			public Smoking(Ingredient ingredient, IItemProvider output, Float experience, int cookingTime) {
				this.ingredient = ingredient;
				this.output = output;
				this.experience = experience;
				this.cookingTime = cookingTime;
			}

			public CookingRecipeBuilder getRecipe() {
				return CookingRecipeBuilder
						.cooking(ingredient, output, experience, cookingTime, IRecipeSerializer.SMOKING_RECIPE)
						.unlockedBy("has_item", DataGenHelper.Criterion.has(ingredient));
			}

		}

		/**
		 * New Campfire Cooking Recipe
		 * @author matyrobbrt
		 *
		 */
		public static class CampfireCooking extends SmeltingRecipe {

			private final Float experience;
			private final int cookingTime;

			/**
			 * Makes a new Campfire Cooking recipe, with 0.35 experience and 600 ticks cook time
			 * @param ingredient the ingredient of the recipe (Ingredient.of(ITEM))
			 * @param output the output of the recipe
			 */
			public CampfireCooking(Ingredient ingredient, IItemProvider output) {
				this.ingredient = ingredient;
				this.output = output;
				this.experience = 0.35f;
				this.cookingTime = 600;
			}

			/**
			 * Makes a new Campfire Cooking recipe
			 * @param ingredient the ingredient of the recipe (Ingredient.of(ITEM))
			 * @param output the output of the recipe
			 * @param experience the amount of experience that the recipe will produce
			 * @param cookingTime the time in ticks the cooking recipe will take
			 */
			public CampfireCooking(Ingredient ingredient, IItemProvider output, Float experience, int cookingTime) {
				this.ingredient = ingredient;
				this.output = output;
				this.experience = experience;
				this.cookingTime = cookingTime;
			}

			public CookingRecipeBuilder getRecipe() {
				return CookingRecipeBuilder
						.cooking(ingredient, output, experience, cookingTime, IRecipeSerializer.CAMPFIRE_COOKING_RECIPE)
						.unlockedBy("has_item", DataGenHelper.Criterion.has(ingredient));
			}

		}
	}

}
