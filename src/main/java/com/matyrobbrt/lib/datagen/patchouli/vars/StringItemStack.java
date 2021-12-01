package com.matyrobbrt.lib.datagen.patchouli.vars;

import net.minecraft.item.ItemStack;

public class StringItemStack {
	
	public final ItemStack stack;
	
	public StringItemStack(ItemStack stack) {
		this.stack = stack;
	}
	
	@Override
	public String toString() {
		return this.stack.getItem().getRegistryName().toString() + "#" + this.stack.getCount() + this.stack.getOrCreateTag().toString();
	}

}
