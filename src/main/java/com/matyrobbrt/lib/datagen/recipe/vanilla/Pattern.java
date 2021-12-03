package com.matyrobbrt.lib.datagen.recipe.vanilla;

import net.minecraft.data.ShapedRecipeBuilder;

/**
 * Class for storing patterns for Vanilla Crafting Recipes
 * 
 * @author matyrobbrt
 *
 */
public class Pattern {

	private final String top;
	private final String middle;
	private final String bottom;

	/**
	 * Defines a Vanilla Crafting Recipe pattern
	 * 
	 * @param top    the top pattern
	 * @param middle the middle pattern
	 * @param bottom the bottom pattern
	 */
	public Pattern(String top, String middle, String bottom) {
		this.top = top;
		this.middle = middle;
		this.bottom = bottom;
	}

	/**
	 * Defines a Vanilla Crafting Recipe pattern
	 * 
	 * @param top the top pattern
	 */
	public Pattern(String top) {
		this.top = top;
		this.middle = "";
		this.bottom = "";
	}

	/**
	 * Gets the top pattern
	 * 
	 * @return the top pattern
	 */
	public String getTop() {
		return this.top;
	}

	/**
	 * Gets the middle pattern
	 * 
	 * @return the middle pattern
	 */
	public String getMiddle() {
		return this.middle;
	}

	/**
	 * Gets the bottom pattern
	 * 
	 * @return the bottom pattern
	 */
	public String getBottom() {
		return this.bottom;
	}

	/**
	 * Applies the pattern to a {@link ShapedRecipeBuilder}
	 * 
	 * @param recipe the recipe to apply the pattern to
	 */
	public void getShapedRecipePattern(ShapedRecipeBuilder recipe) {
		if (this.top != "")
			recipe.pattern(this.top);
		if (this.middle != "")
			recipe.pattern(this.middle);
		if (this.bottom != "")
			recipe.pattern(this.bottom);
	}
}
