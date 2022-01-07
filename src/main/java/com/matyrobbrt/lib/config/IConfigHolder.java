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

package com.matyrobbrt.lib.config;

import java.io.File;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.Logger;

public interface IConfigHolder<C extends IConfigData> extends Supplier<C> {

	@Nonnull
	Class<C> getConfigClass();

	void save();

	boolean load();

	C getConfig();

	File getConfigFile();

	Logger getLogger();

	@Override
	default C get() {
		return getConfig();
	}

	/**
	 * Resets the config held by this holder to its default values. <br>
	 * Does not save the reset config to file, for that use {@link #save()}.
	 */
	void resetToDefault();

	/**
	 * Sets the config held by this holder. <br>
	 * Does not save the set config to file, for that use {@link #save()}.
	 */
	void setConfig(C config);

}
