package com.matyrobbrt.lib.datagen.recipe;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Class for defining outputs of recipes
 * @author matyrobbrt
 *
 */
public class Output {

	private final Item item;
	private final int count;
	
	/**
	 * Creates a new recipe output
	 * @param item the item output
	 * @param count the count of the output
	 */
	public Output(Item item, int count) {
		this.item = item;
		this.count = count;
	}
	
	/**
	 * @see #Output
	 */
	public Output(RegistryObject<Item> item, int count) {
		this.item = item.get();
		this.count = count;
	}
	
	/**
	 * @see #Output
	 */
	public Output(ResourceLocation item, int count) {
		this.item = ForgeRegistries.ITEMS.getValue(item);
		this.count = count;
	}
	
	/**
	 * Gets the output item
	 * @return the output item
	 */
	public Item getItem() {
		return this.item;
	}
	
	/**
	 * Gets the output count
	 * @return the output count
	 */
	public int getCount() {
		return this.count;
	}
	
}
