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

package com.matyrobbrt.lib.config.serializer;

import java.io.ByteArrayInputStream;
import java.nio.file.Path;
import java.util.function.Supplier;

import com.matyrobbrt.lib.config.IConfigData;
import com.matyrobbrt.lib.config.annotation.Config;

public interface IConfigSerializer<C extends IConfigData> {

	void serialize(C config) throws SerializationException;

	C deserialize() throws SerializationException;

	C deserialize(ByteArrayInputStream stream) throws SerializationException;

	C createDefault();

	Supplier<Path> getConfigPath();

	@FunctionalInterface
	interface Factory<T extends IConfigData> {

		IConfigSerializer<T> create(Config configDefinition, Class<T> configClass);
	}

	class SerializationException extends Exception {

		private static final long serialVersionUID = 3221732273308624248L;

		public SerializationException(Throwable cause) {
			super(cause);
		}
	}

}
