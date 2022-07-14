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

package com.matyrobbrt.lib.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;

public class LockableList<E> extends ArrayList<E> {

	private static final long serialVersionUID = 5411121906393882708L;

	private Predicate<E> locked;

	@Override
	public boolean add(E e) {
		if (isLocked(e)) { return false; }
		return super.add(e);
	}

	@Override
	public void add(int index, E element) {
		if (isLocked(element)) { return; }
		super.add(index, element);
	}

	@Override
	public boolean addAll(Collection<? extends E> c) {
		for (E e : c) {
			if (isLocked(e)) { return false; }
		}
		return super.addAll(c);
	}

	@Override
	public boolean addAll(int index, Collection<? extends E> c) {
		for (E e : c) {
			if (isLocked(e)) { return false; }
		}
		return super.addAll(index, c);
	}
	
	public void setLocked(Predicate<E> locked) { this.locked = locked; }
	
	public boolean isLocked(E e) {
		if (locked == null) {
			return false;
		} else {
			return locked.test(e);
		}
	}

}
