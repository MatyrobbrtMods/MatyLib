package com.matyrobbrt.lib.recipe;

import java.util.function.Consumer;

import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.ResourceLocation;

public interface IRecipeBuilder<T extends IRecipe<?>> {

	void build(Consumer<IFinishedRecipe> consumer);

	void build(Consumer<IFinishedRecipe> consumer, ResourceLocation id);

	IRecipeSerializer<T> getSerializer();

}
