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

package com.matyrobbrt.lib.registry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.particles.ParticleType;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvent;

/**
 * The maps stored in here contain all the objects (except {@link BlockItem}s)
 * that are registered using registry annotations. The data is stored as it
 * follows: <br>
 * A {@link List} with the generic type {@code <T>} will be put as the value
 * corresponding to the key which is the namespace (mod id) of the object's
 * registry name. {@code <T>} is the type of the object registered.
 *
 * @author matyrobbrt
 *
 */
public final class MatyLibRegistries {

	public static final Map<String, List<Item>> ITEMS = new HashMap<>();
	public static final Map<String, List<Block>> BLOCKS = new HashMap<>();
	public static final Map<String, List<Fluid>> FLUIDS = new HashMap<>();

	public static final Map<String, List<Effect>> EFFECTS = new HashMap<>();
	public static final Map<String, List<Potion>> POTION_TYPES = new HashMap<>();
	public static final Map<String, List<SoundEvent>> SOUND_EVENTS = new HashMap<>();
	public static final Map<String, List<Attribute>> ATTRIBUTES = new HashMap<>();

	public static final Map<String, List<EntityType<?>>> ENTITY_TYPES = new HashMap<>();
	public static final Map<String, List<TileEntityType<?>>> TILE_ENTITY_TYPES = new HashMap<>();
	public static final Map<String, List<ContainerType<?>>> CONTAINER_TYPES = new HashMap<>();
	public static final Map<String, List<ParticleType<?>>> PARTICLE_TYPES = new HashMap<>();

	public static final Map<String, List<IRecipeType<?>>> RECIPE_TYPES = new HashMap<>();
	public static final Map<String, List<IRecipeSerializer<?>>> RECIPE_SERIALIZERS = new HashMap<>();

}
