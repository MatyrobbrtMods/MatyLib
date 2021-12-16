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

import org.apache.commons.lang3.tuple.Pair;

import com.matyrobbrt.lib.multiblock.wsd.MultiblockDriverWSD;
import com.matyrobbrt.lib.nbt.BaseNBTMap;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.world.level.Level;

/**
 * This class should be used in order to store {@link MultiblockHolder}s in a
 * {@link MultiblockDriverWSD}
 * 
 * @param <T>
 */
public class MultiblockDriver<T extends IMultiblock> {

	private final BaseNBTMap<Integer, MultiblockHolder<T>, IntTag, CompoundTag> multiblocks;
	private int lastId = 0;

	private final Function<CompoundTag, T> deserializer;
	private final BiConsumer<CompoundTag, T> serializer;
	private final Consumer<MultiblockDriver<T>> dirtySetter;
	private final BiPredicate<T, T> mergeChecker;
	private final IMultiblockConnector<T> connector;
	private final BiFunction<Level, BlockPos, IMultiblockComponent> componenetGetter;

	private MultiblockDriver(Builder<T> builder) {
		this.deserializer = builder.deserializer;
		this.serializer = builder.serializer;
		this.dirtySetter = builder.dirtySetter;
		this.mergeChecker = builder.mergeChecker;
		this.connector = builder.connector;
		this.componenetGetter = builder.componentGetter;

		multiblocks = new BaseNBTMap<>(IntTag::valueOf, multiblock -> {
			CompoundTag multiblockTag = new CompoundTag();
			serializer.accept(multiblockTag, multiblock.getMultiblock());
			multiblock.save(multiblockTag);
			return multiblockTag;
		}, IntTag::getAsInt, tag -> {
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

	public BiFunction<Level, BlockPos, IMultiblockComponent> getComponentGetter() { return componenetGetter; }

	public IMultiblockComponent getComponent(Level level, BlockPos pos) {
		return componenetGetter.apply(level, pos);
	}

	public BiPredicate<T, T> getMergeChecker() { return mergeChecker; }

	/**
	 * Create (or replace) a multiblock with a given id and initialize it
	 */
	public MultiblockHolder<T> createOrUpdate(int id, T multiblock) {
		multiblocks.put(id, new MultiblockHolder<>(multiblock));
		dirtySetter.accept(this);
		return getHolder(id);
	}

	public MultiblockHolder<T> getOrCreate(int id, T ifAbsent) {
		multiblocks.computeIfAbsent(id, id2 -> {
			dirtySetter.accept(MultiblockDriver.this);
			return new MultiblockHolder<>(ifAbsent);
		});
		return getHolder(id);
	}

	public MultiblockHolder<T> remove(int id) {
		if (multiblocks.containsKey(id)) {
			MultiblockHolder<T> toReturn = multiblocks.remove(id);
			lastId--;
			dirtySetter.accept(this);
			return toReturn;
		}
		return null;
	}

	@Nullable
	public MultiblockHolder<T> getHolder(int id) {
		return multiblocks.get(id);
	}

	public Integer getIDForPos(BlockPos pos) {
		AtomicReference<Integer> id = new AtomicReference<>(null);
		multiblocks.forEach((mbID, holder) -> {
			if (holder.containsBlockPos(pos)) {
				id.set(mbID);
				return;
			}
		});
		return id.get();
	}

	public Pair<Integer, MultiblockHolder<T>> getHolderForMultiblock(T multiblock) {
		AtomicReference<Pair<Integer, MultiblockHolder<T>>> ref = new AtomicReference<>(null);
		multiblocks.forEach((mbID, holder) -> {
			if (holder.getMultiblock() == multiblock) {
				ref.set(Pair.of(mbID, holder));
			}
		});
		return ref.get();
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
		if (dirtySetter != null) {
			dirtySetter.accept(this);
		}
		return lastId;
	}

	public void load(CompoundTag nbt) {
		clear();
		multiblocks.deserializeNBT(nbt.getCompound("multiblocks"));
		lastId = nbt.getInt("lastId");
	}

	public CompoundTag save(CompoundTag nbt) {
		nbt.put("multiblocks", multiblocks.serializeNBT());
		nbt.putInt("lastId", lastId);
		return nbt;
	}

	public static <T extends IMultiblock> Builder<T> builder() {
		return new Builder<>();
	}

	public static class Builder<T extends IMultiblock> {

		private Function<CompoundTag, T> deserializer;
		private BiConsumer<CompoundTag, T> serializer;
		private Consumer<MultiblockDriver<T>> dirtySetter;
		private BiPredicate<T, T> mergeChecker;
		private IMultiblockConnector<T> connector;
		private BiFunction<Level, BlockPos, IMultiblockComponent> componentGetter;

		public Builder<T> deserializer(Function<CompoundTag, T> deserializer) {
			this.deserializer = deserializer;
			return this;
		}

		public Builder<T> serializer(BiConsumer<CompoundTag, T> serializer) {
			this.serializer = serializer;
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

		public Builder<T> componentGetter(BiFunction<Level, BlockPos, IMultiblockComponent> componenetGetter) {
			this.componentGetter = componenetGetter;
			return this;
		}

		public MultiblockDriver<T> build() {
			return new MultiblockDriver<>(this);
		}
	}

}
