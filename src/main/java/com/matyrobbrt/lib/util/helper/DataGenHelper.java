package com.matyrobbrt.lib.util.helper;

import java.util.Map;

import javax.annotation.Nonnull;

import com.matyrobbrt.lib.datagen.recipe.Output;
import com.matyrobbrt.lib.datagen.recipe.vanilla.KeyIngredient;
import com.matyrobbrt.lib.datagen.recipe.vanilla.Pattern;

import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.InventoryChangeTrigger;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

public class DataGenHelper {

	public static class Criterion {

		public static InventoryChangeTrigger.Instance hasAir() {
			return inventoryTrigger(ItemPredicate.Builder.item().of(Items.AIR).build());
		}

		public static InventoryChangeTrigger.Instance has(IItemProvider item) {
			return inventoryTrigger(ItemPredicate.Builder.item().of(item).build());
		}
		
		public static InventoryChangeTrigger.Instance has(Ingredient ingredient) {
			return inventoryTrigger(ItemPredicate.Builder.item().of(ingredient.getItems()[0].getItem()).build());
		}

		protected static InventoryChangeTrigger.Instance inventoryTrigger(ItemPredicate... p_200405_0_) {
			return new InventoryChangeTrigger.Instance(EntityPredicate.AndPredicate.ANY, MinMaxBounds.IntBound.ANY,
					MinMaxBounds.IntBound.ANY, MinMaxBounds.IntBound.ANY, p_200405_0_);
		}
	}

	/**
	 * Creates a new shaped recipe
	 * 
	 * @param map         the destination of the built recipe
	 * @param output      the output of the recipe
	 * @param pattern     the pattern of the recipe
	 * @param name        the name of the recipe
	 * @param ingredients the ingredients of the recipe
	 */
	public static void newShapedRecipe(Map<ShapedRecipeBuilder, ResourceLocation> map, Output output, Pattern pattern,
			ResourceLocation name, @Nonnull KeyIngredient... ingredients) {
		ShapedRecipeBuilder recipe = new ShapedRecipeBuilder(output.getItem(), output.getCount());
		pattern.getShapedRecipePattern(recipe);
		for (KeyIngredient ingredient : ingredients) {
			ingredient.defineShapedRecipe(recipe);
		}
		map.put(recipe, name);
	}

}
