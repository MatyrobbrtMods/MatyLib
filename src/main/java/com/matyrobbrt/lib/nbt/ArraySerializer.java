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

import java.util.function.Function;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public class ArraySerializer<T, TNBT extends Tag> {

	private final Function<T, TNBT> serializer;
	private final Function<TNBT, T> deserializer;

	public ArraySerializer(Function<T, TNBT> serializer, Function<TNBT, T> deserializer) {
		this.serializer = serializer;
		this.deserializer = deserializer;
	}

	@SuppressWarnings("unchecked")
	public void deserializeNBT(T[] outputArray, CompoundTag nbt) {
		int size = nbt.getInt("size");
		for (int i = 0; i < size; i++) {
			outputArray[i] = deserializer.apply((TNBT) nbt.get(String.valueOf(i)));
		}
	}

	public CompoundTag serializeNBT(T[] inputArray) {
		CompoundTag nbt = new CompoundTag();
		nbt.putInt("size", inputArray.length);
		for (int i = 0; i < inputArray.length; i++) {
			nbt.put(String.valueOf(i), serializer.apply(inputArray[i]));
		}
		return nbt;
	}

}
