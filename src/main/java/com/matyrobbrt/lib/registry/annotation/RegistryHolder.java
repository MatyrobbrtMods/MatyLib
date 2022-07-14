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

package com.matyrobbrt.lib.registry.annotation;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * @deprecated 1.18.2 made this feature unusable, use {@link net.minecraftforge.registries.DeferredRegister} instead
 */
@Documented
@Retention(RUNTIME)
@Target({
		TYPE
})
@Deprecated(forRemoval = true)
public @interface RegistryHolder {

	/**
	 * The modid under which the registries will be registered
	 * 
	 * @return
	 */
	String modid();

	/**
	 * This value controls if the class which has this annotation should be
	 * registered to the {@link AnnotationProcessor} <br>
	 * <strong>BE CAREFUL WITH SWITCHING THIS ON AND OFF BECAUSE IT CAN BREAK
	 * WORLS</strong>
	 * 
	 * @since 1.0.4
	 * @return true if the {@link AnnotationProcessor} should process the class
	 */
	boolean enabled() default true;
}
