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

package com.matyrobbrt.lib.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.matyrobbrt.lib.util.NBTSerializer;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import net.minecraftforge.fluids.capability.templates.FluidTank;

/**
 * Used to sync values from the server to client and vice-versa in tile entities
 */
@Documented
@Retention(RUNTIME)
@Target(FIELD)
public @interface SyncValue {

	public final class Helper {

		private static final Map<Class<?>, NBTSerializer<?>> SERIALIZERS = new HashMap<>();

		/**
		 * Any classes in this (immutable) list CANNOT be registered using
		 * {@link #registerSerializer(Class, Function, BiConsumer)}! This is in order to
		 * not make a mod break the other!
		 */
		private static final List<Class<?>> BLACKLISTED_REGISTERING = List.of(FluidTank.class);

		static {
			SERIALIZERS.put(FluidTank.class,
					NBTSerializer.create(nbt -> new FluidTank(nbt.getInt("TankCapacity")), (nbt, tank) -> {
						tank.writeToNBT(nbt);
						nbt.putInt("TankCapacity", tank.getCapacity());
					}));
		}

		public static boolean serializerExists(Class<?> clazz) {
			return SERIALIZERS.containsKey(clazz);
		}

		/**
		 * Registers a custom serializer for {@link SyncValue}s. Any class in
		 * {@link #BLACKLISTED_REGISTERING} CANNOT have a serializer registered as they
		 * will be registered by default!
		 * 
		 * @param <T>
		 * @param clazz
		 * @param reader
		 * @param writer
		 * @return The old serializer, if exists
		 */
		public static <T> NBTSerializer<?> registerSerializer(Class<T> clazz, Function<CompoundTag, T> reader,
				BiConsumer<CompoundTag, T> writer) {
			if (BLACKLISTED_REGISTERING.contains(clazz)) { return null; }
			return SERIALIZERS.put(clazz, new NBTSerializer<T>() {

				@Override
				public T read(CompoundTag nbt) {
					return reader.apply(nbt);
				}

				@Override
				public void write(CompoundTag nbt, T obj) {
					writer.accept(nbt, obj);
				}
			});
		}

		/**
		 * This method will attempt to read a value from NBT and assign that value for
		 * any field marked with the SyncValue annotation.
		 *
		 * @param fields The fields to sync
		 * @param object The object with SyncValue fields.
		 * @param nbt    The NBT to read values from.
		 */
		public static void readSyncValues(Field[] fields, Object object, CompoundTag nbt) {
			for (Field field : fields) {
				for (Annotation annotation : field.getDeclaredAnnotations()) {
					if (annotation instanceof SyncValue sync) {
						try {
							field.setAccessible(true);
							String name = sync.name();

							if (field.getType() == int.class) {
								field.setInt(object, nbt.getInt(name));
							} else if (field.getType() == float.class) {
								field.setFloat(object, nbt.getFloat(name));
							} else if (field.getType() == String.class) {
								field.set(object, nbt.getString(name));
							} else if (field.getType() == boolean.class) {
								field.setBoolean(object, nbt.getBoolean(name));
							} else if (field.getType() == double.class) {
								field.setDouble(object, nbt.getDouble(name));
							} else if (field.getType() == long.class) {
								field.setLong(object, nbt.getLong(name));
							} else if (field.getType() == short.class) {
								field.setShort(object, nbt.getShort(name));
							} else if (field.getType() == byte.class) {
								field.setByte(object, nbt.getByte(name));
							} else if (SERIALIZERS.containsKey(field.getType())) {
								NBTSerializer<?> serializer = SERIALIZERS.get(field.getType());
								CompoundTag compound = nbt.getCompound(name);
								field.set(object, serializer.read(compound));
							} else {
								throw new IllegalArgumentException("Don't know how to read type " + field.getType()
										+ " from NBT! You can define a custom serializer if you want!");
							}
						} catch (IllegalAccessException | IllegalArgumentException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		/**
		 * Writes sync variables for the object. This method will take the values in all
		 * fields marked with the SyncValue annotation and save them to NBT.
		 *
		 * @param fields   The fields to sync
		 * @param object   The object with SyncValue fields.
		 * @param nbt      The NBT to save values to.
		 * @param syncType The sync type (WRITE or PACKET).
		 * @return The modified NBT.
		 */
		@SuppressWarnings("unchecked") // from serializer
		public static CompoundTag writeSyncValues(Field[] fields, Object object, CompoundTag nbt, SyncType syncType) {

			for (Field field : fields) {
				SyncValue syncAnn = field.getAnnotation(SyncValue.class);

				if (syncType == SyncValue.SyncType.WRITE && syncAnn.onWrite()
						|| syncType == SyncValue.SyncType.PACKET && syncAnn.onPacket()) {
					try {
						field.setAccessible(true);
						String name = syncAnn.name();

						if (field.getType() == int.class) {
							nbt.putInt(name, field.getInt(object));
						} else if (field.getType() == float.class) {
							nbt.putFloat(name, field.getFloat(object));
						} else if (field.getType() == String.class) {
							nbt.putString(name, (String) field.get(object));
						} else if (field.getType() == boolean.class) {
							nbt.putBoolean(name, field.getBoolean(object));
						} else if (field.getType() == double.class) {
							nbt.putDouble(name, field.getDouble(object));
						} else if (field.getType() == long.class) {
							nbt.putLong(name, field.getLong(object));
						} else if (field.getType() == short.class) {
							nbt.putShort(name, field.getShort(object));
						} else if (field.getType() == byte.class) {
							nbt.putByte(name, field.getByte(object));
						} else if (SERIALIZERS.containsKey(field.getType())) {
							CompoundTag compound = new CompoundTag();
							@SuppressWarnings("rawtypes")
							NBTSerializer serializer = SERIALIZERS.get(field.getType());
							serializer.write(compound, field.get(object));
							nbt.put(name, compound);
						} else {
							throw new IllegalArgumentException("Don't know how to write type " + field.getType()
									+ " to NBT! You can define a custom serializer if you want!");
						}
					} catch (IllegalAccessException | IllegalArgumentException e) {
						e.printStackTrace();
					}
				}
			}
			return nbt;
		}

		private Helper() {
			throw new IllegalAccessError("Helper class!");
		}

	}

	/**
	 * Used together with onRead, onWrite, and onPacket to determine when a variable
	 * should be saved/loaded. In most cases, you should probably just sync
	 * everything at all times.
	 */
	enum SyncType {
		READ, WRITE, PACKET
	}

	/**
	 * The name to read/write to NBT.
	 *
	 * @return the variables NBT key (name)
	 */
	String name();

	/**
	 * Should the variable be saved in {@link BlockEntity#getUpdatePacket()} and
	 * {@link BlockEntity#getUpdateTag()}? <br>
	 * <strong>IMPORTANT: This may clutter the network, so only use it if
	 * necessary!</strong>
	 *
	 * @return true if we should save on sync packet
	 */
	boolean onPacket() default false;

	/**
	 * Should the variable be loaded in
	 * {@link BlockEntity#loadStatic(net.minecraft.core.BlockPos, BlockState, CompoundTag)}?
	 *
	 * @return true if we should load on read
	 */
	boolean onRead()

	default true;

	/**
	 * Should the variable be saved in {@link BlockEntity#save(CompoundTag)}?
	 *
	 * @return true if we should save on write
	 */
	boolean onWrite()

	default true;
}
