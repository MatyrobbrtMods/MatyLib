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

package com.matyrobbrt.lib.util.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

import net.minecraftforge.registries.ForgeRegistries;

public class NBTHelper {
	
	public static BlockPos getBlockPos(ItemStack stack, String key) {
		CompoundTag nbt = getTagCompound(stack).getCompound(key);
		boolean hasData = nbt.getDouble("x") != 0 && nbt.getDouble("y") != 0 && nbt.getDouble("z") != 0;
		return hasData ? new BlockPos(nbt.getDouble("x"), nbt.getDouble("y"), nbt.getDouble("z")) : null;
	}
	
	public static void setBlockPos(ItemStack stack, String key, BlockPos value) {
		CompoundTag nbt = new CompoundTag();
		nbt.putDouble("x", value.getX());
		nbt.putDouble("y", value.getY());
		nbt.putDouble("z", value.getZ());
		getTagCompound(stack).put(key, nbt);
	}
	
	public static boolean getBoolean(ItemStack stack, String key) {
		return stack.hasTag() && getTagCompound(stack).getBoolean(key);
	}

	public static void flipBoolean(ItemStack stack, String key) {
		setBoolean(stack, key, !getBoolean(stack, key));
	}

	public static void setBoolean(ItemStack stack, String key, boolean value) {
		getTagCompound(stack).putBoolean(key, value);
	}

	public static void createCompound(ItemStack stack) {
		if (!stack.hasTag()) {
			CompoundTag tag = new CompoundTag();
			stack.setTag(tag);
		}
	}

	public static void setString(ItemStack stack, String key, String value) {
		getTagCompound(stack).putString(key, value);
	}

	public static String getString(ItemStack stack, String key) {
		return stack.hasTag() ? getTagCompound(stack).getString(key) : "";
	}

	public static CompoundTag getTagCompound(ItemStack stack) {
		createCompound(stack);
		return stack.getTag();
	}

	public static void setInt(ItemStack stack, String key, int value) {
		getTagCompound(stack).putInt(key, value);
	}

	public static int getInt(ItemStack stack, String key) {
		return stack.hasTag() ? getTagCompound(stack).getInt(key) : 0;
	}

	public static double getDouble(ItemStack stack, String key) {
		return stack.hasTag() ? getTagCompound(stack).getDouble(key) : 0.0;
	}

	public static void setDouble(ItemStack stack, String key, double value) {
		getTagCompound(stack).putDouble(key, value);
	}

	public static double getFloat(ItemStack stack, String key) {
		return stack.hasTag() ? getTagCompound(stack).getFloat(key) : 0.0F;
	}

	public static void setFloat(ItemStack stack, String key, float value) {
		getTagCompound(stack).putFloat(key, value);
	}

	public static Item getItem(ItemStack stack, String key) {
		return ForgeRegistries.ITEMS.getValue(new ResourceLocation(getString(stack, key)));
	}

	public static void setItem(ItemStack stack, String key, ItemLike item) {
		setString(stack, key, item.asItem().getRegistryName().toString());
	}

}
