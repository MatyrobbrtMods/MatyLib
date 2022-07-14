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

package com.matyrobbrt.lib.datagen.recipe.vanilla;

import net.minecraft.data.recipes.ShapedRecipeBuilder;

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
