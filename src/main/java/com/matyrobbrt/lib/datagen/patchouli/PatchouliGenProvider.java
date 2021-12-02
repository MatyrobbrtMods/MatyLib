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

package com.matyrobbrt.lib.datagen.patchouli;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;

import javax.annotation.Nullable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.matyrobbrt.lib.datagen.patchouli.type.PatchouliBook;
import com.matyrobbrt.lib.datagen.patchouli.type.PatchouliCategory;
import com.matyrobbrt.lib.datagen.patchouli.type.PatchouliEntry;

import net.minecraft.data.DataGenerator;
import net.minecraft.data.DirectoryCache;
import net.minecraft.data.IDataProvider;

public abstract class PatchouliGenProvider implements IDataProvider {

	public static final Gson GSON = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
	private static final Logger LOGGER = LogManager.getLogger();
	public final DataGenerator generator;

	public final String modid;
	public final String language;
	public final String bookName;

	public ArrayList<PatchouliEntry> entries = new ArrayList<>();
	public ArrayList<PatchouliCategory> categories = new ArrayList<>();

	protected PatchouliGenProvider(DataGenerator generator, String modid, String language, String bookName) {
		this.generator = generator;
		this.modid = modid;
		this.language = language;
		this.bookName = bookName;
	}

	@Override
	public void run(DirectoryCache pCache) throws IOException {
		try {
			writeBook(pCache);
		} catch (Exception e1) {
			e1.printStackTrace();
		}

		addEntries();
		writeEntries(pCache);

		addCategories();
		try {
			writeCategories(pCache);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void writeEntries(DirectoryCache cache) {
		Path outputFolder = generator.getOutputFolder();
		entries.forEach(entry -> {
			Path path = outputFolder.resolve("data/" + modid + "/patchouli_books/" + bookName + "/" + language
					+ "/entries/" + entry.category + "/" + entry.fileName + ".json");
			try {
				IDataProvider.save(GSON, cache, entry.serialize(), path);
			} catch (IOException e) {
				LOGGER.error("Couldn't generate entry!", path, e);
			}
		});
	}

	private void writeCategories(DirectoryCache cache) throws Exception {
		Path outputFolder = generator.getOutputFolder();
		Class<?> clazz = this.getClass();
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(PatchouliCategoryGen.class)) {
				field.setAccessible(true);
				if (field.getType() == PatchouliCategory.class) {
					PatchouliCategory category = (PatchouliCategory) field.get(clazz);
					Path path = outputFolder.resolve("data/" + modid + "/patchouli_books/" + bookName + "/" + language
							+ "/categories/" + category.fileName + ".json");
					try {
						IDataProvider.save(GSON, cache, category.serialize(), path);
					} catch (IOException e) {
						LOGGER.error("Couldn't generate category!", path, e);
					}
				}
			}
		}
		categories.forEach(category -> {
			Path path = outputFolder.resolve("data/" + modid + "/patchouli_books/" + bookName + "/" + language
					+ "/categories/" + category.fileName + ".json");
			try {
				IDataProvider.save(GSON, cache, category.serialize(), path);
			} catch (IOException e) {
				LOGGER.error("Couldn't generate category!", path, e);
			}
		});
	}

	private void writeBook(DirectoryCache cache) throws Exception {
		Path outputFolder = generator.getOutputFolder();
		Class<?> clazz = this.getClass();
		for (Field field : clazz.getDeclaredFields()) {
			if (field.isAnnotationPresent(PatchouliBookGen.class)) {
				field.setAccessible(true);
				if (field.getType() == PatchouliBook.class) {
					PatchouliBook book = (PatchouliBook) field.get(clazz);
					Path path = outputFolder
							.resolve("data/" + modid + "/patchouli_books/" + bookName + "/" + "book.json");
					try {
						IDataProvider.save(GSON, cache, book.serialize(), path);
					} catch (IOException e) {
						LOGGER.error("Couldn't generate book!", path, e);
					}
				}
			}
		}
	}

	@Nullable
	public void addEntries() {
	}

	@Nullable
	public void addCategories() {
	}

	@Override
	public String getName() { return "PatchouliGenProvider"; }

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	protected @interface PatchouliCategoryGen {

	}

	@Retention(RetentionPolicy.RUNTIME)
	@Target(ElementType.FIELD)
	protected @interface PatchouliBookGen {

	}

}
