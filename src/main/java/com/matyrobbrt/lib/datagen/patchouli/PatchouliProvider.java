/**
 * This file is part of the MatyLib Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
 * <p>
 * MIT License
 * <p>
 * Copyright (c) 2022 Matyrobbrt
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.matyrobbrt.lib.datagen.patchouli;

import com.matyrobbrt.lib.datagen.patchouli.type.PatchouliBook;
import com.matyrobbrt.lib.datagen.patchouli.type.PatchouliCategory;
import com.matyrobbrt.lib.datagen.patchouli.type.PatchouliEntry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Provider for Patchouli Data Generators
 *
 * @author matyrobbrt
 *
 */
public abstract class PatchouliProvider implements DataProvider {

    private static final Logger LOGGER = LogManager.getLogger();
    public final DataGenerator generator;

    public final String modid;
    public final String language;
    public final String bookName;
    protected boolean useResourcePack = true;

    public ArrayList<PatchouliEntry> entries = new ArrayList<>();
    public ArrayList<PatchouliCategory> categories = new ArrayList<>();

    protected PatchouliProvider(DataGenerator generator, String modid, String language, String bookName) {
        this.generator = generator;
        this.modid = modid;
        this.language = language;
        this.bookName = bookName;
    }

    @Override
    public void run(@NotNull CachedOutput pCache) {
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

    private void writeEntries(CachedOutput cache) {
        Path outputFolder = generator.getOutputFolder();
        entries.forEach(entry -> {
            final var folder = useResourcePack ? "assets" : "data";
            Path path = outputFolder.resolve(folder + "/" + modid + "/patchouli_books/" + bookName + "/" + language
                    + "/entries/" + new ResourceLocation(entry.category).getPath() + "/" + entry.fileName + ".json");
            try {
                DataProvider.saveStable(cache, entry.serialize(), path);
            } catch (IOException e) {
                LOGGER.error("Couldn't generate entry at {}!", path, e);
            }
        });
    }

    private void writeCategories(CachedOutput cache) throws Exception {
        Path outputFolder = generator.getOutputFolder();
        Class<?> clazz = this.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(PatchouliCategoryGen.class)) {
                field.setAccessible(true);
                if (field.getType() == PatchouliCategory.class) {
                    final var folder = useResourcePack ? "assets" : "data";
                    PatchouliCategory category = (PatchouliCategory) field.get(this);
                    Path path = outputFolder.resolve(folder + "/" + modid + "/patchouli_books/" + bookName + "/" + language
                            + "/categories/" + category.fileName + ".json");
                    try {
                        DataProvider.saveStable(cache, category.serialize(), path);
                    } catch (IOException e) {
                        LOGGER.error("Couldn't generate category at {}!", path, e);
                    }
                }
            }
        }
        categories.forEach(category -> {
            final var folder = useResourcePack ? "assets" : "data";
            Path path = outputFolder.resolve(folder + "/" + modid + "/patchouli_books/" + bookName + "/" + language
                    + "/categories/" + category.fileName + ".json");
            try {
                DataProvider.saveStable(cache, category.serialize(), path);
            } catch (IOException e) {
                LOGGER.error("Couldn't generate category at {}!", path, e);
            }
        });
    }

    @Nullable
    protected PatchouliBook getBook() throws Exception {
        Class<?> clazz = this.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(PatchouliBookGen.class)) {
                field.setAccessible(true);
                if (field.getType() == PatchouliBook.class) {
                    return (PatchouliBook) field.get(this);
                }
            }
        }
        return null;
    }

    private void writeBook(CachedOutput cache) throws Exception {
        final var book = getBook();
        if (book != null) {
            Path outputFolder = generator.getOutputFolder();
            Path path = outputFolder
                    .resolve("data/" + modid + "/patchouli_books/" + bookName + "/" + "book.json");
            try {
                DataProvider.saveStable(cache, book.serialize(useResourcePack), path);
            } catch (IOException e) {
                LOGGER.error("Couldn't generate book to {}!", path, e);
            }
        }
    }

    public void addEntries() {
    }

    public void addCategories() {
    }

    @Override
    public @NotNull String getName() {
        return "PatchouliGenProvider";
    }

    /**
     * Annotate a {@link PatchouliCategory} with this annotation in order to
     * register it automatically
     *
     * @author matyrobbrt
     *
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    protected @interface PatchouliCategoryGen {

    }

    /**
     * Annotate a {@link PatchouliBook} with this annotation in order to register it
     * automatically
     *
     * @author matyrobbrt
     *
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    protected @interface PatchouliBookGen {

    }

}
