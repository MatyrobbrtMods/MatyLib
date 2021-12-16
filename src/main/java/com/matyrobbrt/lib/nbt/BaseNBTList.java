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

package com.matyrobbrt.lib.nbt;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

import net.minecraftforge.common.util.INBTSerializable;

@SuppressWarnings("unchecked")
public class BaseNBTList<O, OTAG extends Tag> extends ArrayList<O> implements INBTSerializable<CompoundTag> {

	private static final long serialVersionUID = -8221947185139769286L;

	private final transient Function<O, OTAG> serializer;
	private final transient Function<OTAG, O> deserializer;

	public BaseNBTList(Function<O, OTAG> serializer, Function<OTAG, O> deserializer) {
		super();
		this.serializer = serializer;
		this.deserializer = deserializer;
	}

	public BaseNBTList(Function<O, OTAG> serializer, Function<OTAG, O> deserializer, List<O> other) {
		super(other);
		this.serializer = serializer;
		this.deserializer = deserializer;
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag tag = new CompoundTag();
		tag.putInt("size", size());
		for (int i = 0; i < size(); i++) {
			tag.put(String.valueOf(i), serializer.apply(get(i)));
		}
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		int size = nbt.getInt("size");
		for (int i = 0; i < size; i++) {
			O element = deserializer.apply((OTAG) nbt.get(String.valueOf(i)));
			if (i < size()) {
				set(i, element);
			} else {
				add(i, element);
			}
		}
	}

	@Override
	public boolean equals(Object o) {
		return super.equals(o) && o instanceof BaseNBTList<?, ?>;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

}
