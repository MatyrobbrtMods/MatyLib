package com.matyrobbrt.lib.annotation;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Annotation;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import com.matyrobbrt.lib.util.NBTSerializer;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

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
		 * This method will attempt to read a value from NBT and assign that value for
		 * any field marked with the SyncValue annotation.
		 *
		 * @param fields The fields to sync
		 * @param object The object with SyncValue fields.
		 * @param nbt    The NBT to read values from.
		 */
		public static void readSyncValues(Field[] fields, Object object, CompoundNBT nbt) {
			for (Field field : fields) {
				for (Annotation annotation : field.getDeclaredAnnotations()) {
					if (annotation instanceof SyncValue) {
						SyncValue sync = (SyncValue) annotation;

						try {
							if (!field.isAccessible()) {
								field.setAccessible(true);
							}
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
								CompoundNBT compound = nbt.getCompound(name);
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

		public static <T> void registerSerializer(Class<T> clazz, Function<CompoundNBT, T> reader,
				BiConsumer<CompoundNBT, T> writer) {
			SERIALIZERS.put(clazz, new NBTSerializer<T>() {

				@Override
				public T read(CompoundNBT nbt) {
					return reader.apply(nbt);
				}

				@Override
				public void write(CompoundNBT nbt, T obj) {
					writer.accept(nbt, obj);
				}
			});
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
		public static CompoundNBT writeSyncValues(Field[] fields, Object object, CompoundNBT nbt, SyncType syncType) {

			for (Field field : fields) {
				SyncValue syncAnn = field.getAnnotation(SyncValue.class);

				if (syncType == SyncValue.SyncType.WRITE && syncAnn.onWrite()
						|| syncType == SyncValue.SyncType.PACKET && syncAnn.onPacket()) {
					try {
						if (!field.isAccessible()) {
							field.setAccessible(true);
						}
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
							CompoundNBT compound = new CompoundNBT();
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
	 * Should the variable be saved in {@link TileEntity#getUpdatePacket()} and
	 * {@link TileEntity#getUpdateTag()}? <br>
	 * <strong>IMPORTANT: This may clutter the network, so only use it if
	 * necessary!</strong>
	 *
	 * @return true if we should save on sync packet
	 */
	boolean onPacket() default false;

	/**
	 * Should the variable be loaded in
	 * {@link TileEntity#loadStatic(BlockState, CompoundNBT)}?
	 *
	 * @return true if we should load on read
	 */
	boolean onRead()

	default true;

	/**
	 * Should the variable be saved in {@link TileEntity#save(CompoundNBT)}?
	 *
	 * @return true if we should save on write
	 */
	boolean onWrite()

	default true;
}
