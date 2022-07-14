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

package com.matyrobbrt.lib.util.helper;

import java.util.Map;

import javax.annotation.Nonnull;

import com.matyrobbrt.lib.datagen.recipe.Output;
import com.matyrobbrt.lib.datagen.recipe.vanilla.KeyIngredient;
import com.matyrobbrt.lib.datagen.recipe.vanilla.Pattern;

import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

public class DataGenHelper {

	public static class Criterion {

		public static TriggerInstance hasAir() {
			return inventoryTrigger(ItemPredicate.Builder.item().of(Items.AIR).build());
		}

		public static TriggerInstance has(ItemLike item) {
			return inventoryTrigger(ItemPredicate.Builder.item().of(item).build());
		}
		
		public static TriggerInstance has(Ingredient ingredient) {
			return inventoryTrigger(ItemPredicate.Builder.item().of(ingredient.getItems()[0].getItem()).build());
		}

		protected static TriggerInstance inventoryTrigger(ItemPredicate... p_200405_0_) {
			return new TriggerInstance(EntityPredicate.Composite.ANY, MinMaxBounds.Ints.ANY, MinMaxBounds.Ints.ANY,
					MinMaxBounds.Ints.ANY, p_200405_0_);
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
