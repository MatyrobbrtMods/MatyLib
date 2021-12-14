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

package com.matyrobbrt.lib.multiblock;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import java.util.function.Function;

import javax.annotation.Nullable;

import com.matyrobbrt.lib.multiblock.wsd.MultiblockDriverWSD;
import com.matyrobbrt.lib.nbt.BaseNBTMap;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.IntNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * This class should be used in order to store {@link MultiblockHolder}s in a
 * {@link MultiblockDriverWSD}
 * 
 * @param <T>
 */
public class MultiblockDriver<T extends IMultiblock> {

	private final BaseNBTMap<Integer, MultiblockHolder<T>, IntNBT, CompoundNBT> multiblocks;
	private int lastId = 0;

	private final Function<CompoundNBT, T> deserializer;
	private final BiConsumer<CompoundNBT, T> serializer;
	private final Consumer<MultiblockDriver<T>> dirtySetter;
	private final BiPredicate<T, T> mergeChecker;
	private final IMultiblockConnector<T> connector;
	private final BiFunction<World, BlockPos, IMultiblockComponent> componenetGetter;

	private MultiblockDriver(Builder<T> builder) {
		this.deserializer = builder.deserializer;
		this.serializer = builder.serializer;
		this.dirtySetter = builder.dirtySetter;
		this.mergeChecker = builder.mergeChecker;
		this.connector = builder.connector;
		this.componenetGetter = builder.componentGetter;

		multiblocks = new BaseNBTMap<>(IntNBT::valueOf, multiblock -> {
			CompoundNBT multiblockTag = new CompoundNBT();
			serializer.accept(multiblockTag, multiblock.getMultiblock());
			multiblock.save(multiblockTag);
			return multiblockTag;
		}, IntNBT::getAsInt, tag -> {
			T value = deserializer.apply(tag);
			MultiblockHolder<T> holder = new MultiblockHolder<>(value);
			holder.load(tag);
			return holder;
		});
	}

	public void clear() {
		multiblocks.clear();
		lastId = 0;
	}

	public IMultiblockConnector<T> getConnector() { return connector; }

	public BiFunction<World, BlockPos, IMultiblockComponent> getComponentGetter() { return componenetGetter; }

	public IMultiblockComponent getComponent(World level, BlockPos pos) {
		return componenetGetter.apply(level, pos);
	}

	public BiPredicate<T, T> getMergeChecker() { return mergeChecker; }

	/**
	 * Create (or replace) a multiblock with a given id and initialize it
	 */
	public MultiblockHolder<T> createOrUpdate(int id, T multiblock) {
		multiblocks.put(id, new MultiblockHolder<>(multiblock));
		return getHolder(id);
	}

	public MultiblockHolder<T> getOrCreate(int id, T ifAbsent) {
		multiblocks.computeIfAbsent(id, id2 -> new MultiblockHolder<>(ifAbsent));
		return getHolder(id);
	}

	@Nullable
	public MultiblockHolder<T> getHolder(int id) {
		return multiblocks.get(id);
	}

	public int getIDForPos(BlockPos pos) {
		AtomicReference<Integer> id = new AtomicReference<>(null);
		multiblocks.forEach((mbID, holder) -> {
			if (holder.containsBlockPos(pos)) {
				id.set(mbID);
				return;
			}
		});
		return id.get();
	}

	/**
	 * Get a multiblock with a given id. Returns null if the multiblock doesn't
	 * exist
	 */
	@Nullable
	public T get(int id) {
		MultiblockHolder<T> holder = multiblocks.get(id);
		return holder == null ? null : holder.getMultiblock();
	}

	/**
	 * Delete a multiblock
	 */
	public void delete(int id) {
		multiblocks.remove(id);
		dirtySetter.accept(this);
	}

	/**
	 * Modify a multiblock (holder) and make sure the data gets saved afterwards
	 */
	public void modify(int id, Consumer<MultiblockHolder<T>> consumer) {
		MultiblockHolder<T> holder = multiblocks.get(id);
		if (holder != null) {
			consumer.accept(holder);
			dirtySetter.accept(this);
		}
	}

	/**
	 * Create a new multiblock ID
	 */
	public int createId() {
		lastId++;
		dirtySetter.accept(this);
		return lastId;
	}

	public void load(CompoundNBT nbt) {
		clear();
		multiblocks.deserializeNBT(nbt.getCompound("multiblocks"));
		lastId = nbt.getInt("lastId");
	}

	public CompoundNBT save(CompoundNBT nbt) {
		nbt.put("multiblocks", multiblocks.serializeNBT());
		nbt.putInt("lastId", lastId);
		return nbt;
	}

	public static <T extends IMultiblock> Builder<T> builder() {
		return new Builder<>();
	}

	public static class Builder<T extends IMultiblock> {

		private Function<CompoundNBT, T> deserializer;
		private BiConsumer<CompoundNBT, T> serializer;
		private Consumer<MultiblockDriver<T>> dirtySetter;
		private BiPredicate<T, T> mergeChecker;
		private IMultiblockConnector<T> connector;
		private BiFunction<World, BlockPos, IMultiblockComponent> componentGetter;

		public Builder<T> deserializer(Function<CompoundNBT, T> deserializer) {
			this.deserializer = deserializer;
			return this;
		}

		public Builder<T> deserializer(BiConsumer<CompoundNBT, T> deserializer) {
			this.serializer = deserializer;
			return this;
		}

		public Builder<T> dirtySetter(Consumer<MultiblockDriver<T>> dirtySetter) {
			this.dirtySetter = dirtySetter;
			return this;
		}

		public Builder<T> mergeChecker(BiPredicate<T, T> mergeChecker) {
			this.mergeChecker = mergeChecker;
			return this;
		}

		public Builder<T> connector(IMultiblockConnector<T> connector) {
			this.connector = connector;
			return this;
		}

		public Builder<T> componentGetter(BiFunction<World, BlockPos, IMultiblockComponent> componenetGetter) {
			this.componentGetter = componenetGetter;
			return this;
		}

		public MultiblockDriver<T> build() {
			return new MultiblockDriver<>(this);
		}
	}

}
