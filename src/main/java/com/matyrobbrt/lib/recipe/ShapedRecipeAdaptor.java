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

package com.matyrobbrt.lib.recipe;

import javax.annotation.Nonnull;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import net.minecraftforge.common.crafting.IShapedRecipe;

public abstract class ShapedRecipeAdaptor implements CraftingRecipe, IShapedRecipe<CraftingContainer> {

	private final ShapedRecipe recipe;

	protected ShapedRecipeAdaptor(ShapedRecipe recipe) {
		this.recipe = recipe;
	}

	@Override
	public boolean canCraftInDimensions(int width, int height) {
		return recipe.canCraftInDimensions(width, height);
	}

	@Override
	@Nonnull
	public NonNullList<ItemStack> getRemainingItems(@Nonnull CraftingContainer inv) {
		return recipe.getRemainingItems(inv);
	}

	@Override
	@Nonnull
	public NonNullList<Ingredient> getIngredients() { return recipe.getIngredients(); }

	@Override
	public boolean isSpecial() { return recipe.isSpecial(); }

	@Override
	@Nonnull
	public String getGroup() { return recipe.getGroup(); }

	@Override
	@Nonnull
	public ItemStack getToastSymbol() { return recipe.getToastSymbol(); }

	public ShapedRecipe getRecipe() { return recipe; }

	@Override
	public int getRecipeWidth() { return recipe.getRecipeWidth(); }

	@Override
	public int getRecipeHeight() { return recipe.getRecipeHeight(); }

	@Override
	public boolean matches(@Nonnull CraftingContainer inv, @Nonnull Level level) {
		return recipe.matches(inv, level);
	}

	@Override
	@Nonnull
	public ItemStack assemble(@Nonnull CraftingContainer inv) {
		return recipe.assemble(inv);
	}

	@Override
	@Nonnull
	public ItemStack getResultItem() { return recipe.getResultItem(); }

	@Override
	@Nonnull
	public ResourceLocation getId() { return recipe.getId(); }

	@Override
	@Nonnull
	public RecipeType<?> getType() { return recipe.getType(); }

}
