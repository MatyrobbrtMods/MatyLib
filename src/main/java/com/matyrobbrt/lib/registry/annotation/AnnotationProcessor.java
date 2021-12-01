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

package com.matyrobbrt.lib.registry.annotation;

import static com.matyrobbrt.lib.registry.MatyLibRegistries.*;
import static java.util.Optional.of;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.objectweb.asm.Type;

import com.google.common.collect.Lists;
import com.matyrobbrt.lib.annotation.RL;
import com.matyrobbrt.lib.module.IModule;
import com.matyrobbrt.lib.module.Module;
import com.matyrobbrt.lib.registry.BetterRegistryObject;
import com.matyrobbrt.lib.registry.MatyLibRegistries;
import com.matyrobbrt.lib.registry.annotation.recipe.RegisterRecipeSerializer;
import com.matyrobbrt.lib.registry.annotation.recipe.RegisterRecipeType;
import com.matyrobbrt.lib.util.ReflectionHelper;
import com.matyrobbrt.lib.util.TriFunction;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.particles.ParticleType;
import net.minecraft.potion.Effect;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.registry.Registry;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * Handles all of the RegistryAnnotations (all annotations included in this
 * package {@link com.machina.api.annotation.registries}), and {@link Module}
 * annotations (and such it handles {@link IModule}s as well). <br>
 * Any exception that will be caught during the processing <strong>will be
 * thrown back</strong> as a {@link ProcessingException}
 *
 * @author matyrobbrt
 *
 */
public class AnnotationProcessor {

	protected final String ownerModID;
	protected IEventBus modBus;
	protected IEventBus forgeBus;

	protected Function<Block, ItemGroup> autoBlockItemTab = block -> ItemGroup.TAB_MISC;

	protected final HashMap<String, Class<?>> registryClasses = new HashMap<>();

	protected final ArrayList<Class<?>> moduleClasses = new ArrayList<>();
	protected final HashMap<ResourceLocation, IModule> modules = new HashMap<>();

	protected Runnable afterInit = () -> {

	};

	/**
	 * Creates a new {@link AnnotationProcessor} which will be used in order to
	 * process annotations. It is recommended to store this statically somewhere.
	 *
	 * @param modid the mod id to process the annotations for
	 */
	public AnnotationProcessor(String modid) {
		ownerModID = modid;
	}

	public void setAutoBlockItemTab(Function<Block, ItemGroup> tab) { this.autoBlockItemTab = tab; }

	public void afterInit(Runnable toRun) {
		this.afterInit = toRun;
	}

	public List<Item> getItems() {
		return MatyLibRegistries.ITEMS.get(ownerModID) != null ? MatyLibRegistries.ITEMS.get(ownerModID)
				: Lists.newArrayList();
	}

	public List<Block> getBlocks() {
		return MatyLibRegistries.BLOCKS.get(ownerModID) != null ? MatyLibRegistries.BLOCKS.get(ownerModID)
				: Lists.newArrayList();
	}

	public HashMap<ResourceLocation, IModule> getModules() { return modules; }

	/**
	 * Adds listeners for all the methods that process annotations. Basically starts
	 * the actual registering. <br>
	 * Call it in your mods' constructor
	 *
	 * @param modBus
	 */
	public void register(IEventBus modBus) {
		this.forgeBus = MinecraftForge.EVENT_BUS;
		this.modBus = modBus;

		modBus.addListener(this::constructMod);

		// Registry Annotations
		modBus.addGenericListener(Block.class, this::registerBlocks);
		modBus.addGenericListener(ContainerType.class, this::registerContainerTypes);
		modBus.addGenericListener(Effect.class, this::registerEffects);
		modBus.addGenericListener(Fluid.class, this::registerFluids);
		modBus.addGenericListener(Item.class, this::registerItems);
		modBus.addGenericListener(ParticleType.class, this::registerParticleTypes);
		modBus.addGenericListener(SoundEvent.class, this::registerSoundEvents);
		modBus.addGenericListener(IRecipeSerializer.class, this::registerRecipeTypes);
		modBus.addGenericListener(TileEntityType.class, this::registerTileEntityTypes);
		modBus.addListener(this::registerCustomRegistries);
	}

	private void constructMod(FMLConstructModEvent event) {
		initModules();
		modules.forEach((id, module) -> module.register(modBus, forgeBus));
		initRegistryClasses();
		afterInit.run();
	}

	private void initRegistryClasses() {
		final List<ModFileScanData.AnnotationData> regAnnotations = ModList.get().getAllScanData().stream()
				.map(ModFileScanData::getAnnotations).flatMap(Collection::stream)
				.filter(a -> a.getAnnotationType().equals(Type.getType(RegistryHolder.class)))
				.collect(Collectors.toList());

		regAnnotations.stream().filter(a -> Type.getType(RegistryHolder.class).equals(a.getAnnotationType()))
				.filter(a -> a.getTargetType() == ElementType.TYPE).forEach(data -> {
					try {
						Class<?> clazz = Class.forName(data.getClassType().getClassName(), false,
								getClass().getClassLoader());
						if (clazz.getAnnotation(RegistryHolder.class).modid().equals(ownerModID)) {
							if (!registryClasses.containsValue(clazz)) {
								registryClasses.put(clazz.getAnnotation(RegistryHolder.class).modid(), clazz);
							}
						}
					} catch (ClassNotFoundException e) {
						throw new ProcessingException("A class which holds registry annotations was not found!", e);
					}
				});
	}

	private void initModules() {
		final List<ModFileScanData.AnnotationData> moduleAnnotations = ModList.get().getAllScanData().stream()
				.map(ModFileScanData::getAnnotations).flatMap(Collection::stream)
				.filter(a -> a.getAnnotationType().equals(Type.getType(Module.class))).collect(Collectors.toList());

		moduleAnnotations.stream().filter(a -> Type.getType(Module.class).equals(a.getAnnotationType()))
				.filter(a -> a.getTargetType() == ElementType.TYPE).forEach(data -> {
					try {
						Class<?> clazz = Class.forName(data.getClassType().getClassName(), false,
								getClass().getClassLoader());
						RL id = clazz.getAnnotation(Module.class).id();
						if (id.modid().equals(ownerModID)) {
							moduleClasses.add(clazz);
							registryClasses.put(id.modid(), clazz);
							Constructor<?> constructor = clazz.getConstructor();
							constructor.setAccessible(true);
							Object instance = constructor.newInstance();
							if (instance instanceof IModule) {
								ResourceLocation name = new ResourceLocation(id.modid(), id.path());
								if (!modules.containsKey(name)) {
									modules.put(name, (IModule) instance);
								} else {
									throw new ProcessingException("Duplicate module id " + name);
								}
							} else {
								throw new ProcessingException(
										"The module " + clazz + " is not an instace of " + IModule.class);
							}
						}
					} catch (IllegalAccessError | InstantiationException | IllegalAccessException
							| NoSuchMethodException | InvocationTargetException e) {
						throw new ProcessingException(e);
					} catch (ClassNotFoundException e) {
						throw new ProcessingException("A class which is a module was not found!", e);
					} catch (SecurityException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				});

	}

	// Registry Annotations

	private void registerItems(final RegistryEvent.Register<Item> event) {
		registerFieldsWithAnnotation(event, RegisterItem.class, RegisterItem::value, of(ITEMS));
		AnnotationProcessor.registerFieldsWithAnnotation(Lists.newArrayList(registryClasses.values()), event,
				RegisterBlockItem.class, (classAn, fieldAn, obj) -> {
					if (obj instanceof BlockItem) { return ((BlockItem) obj).getBlock().getRegistryName(); }
					throw new ProcessingException("Invalid BlockItem");
				}, Optional.empty());
		registerAutoBIs(event);
	}

	private void registerAutoBIs(final RegistryEvent.Register<Item> event) {
		ReflectionHelper.getFieldsAnnotatedWith(Lists.newArrayList(registryClasses.values()), AutoBlockItem.class)
				.forEach(field -> {
					try {
						Object value = field.get(field.getDeclaringClass());
						if (value instanceof Block) {
							Block block = (Block) value;
							BlockItem item = new BlockItem(block,
									new Item.Properties().tab(autoBlockItemTab.apply(block)));
							event.getRegistry().register(item.setRegistryName(block.getRegistryName()));
						} else {
				//@formatter:off
					throw new ProcessingException("The field " + field + " is annotated with @AutoBlockItem but it is not a block!");
					//@formatter:on
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
						throw new ProcessingException("Registry Annotations Failed!", e);
					}
				});
	}

	private void registerBlocks(final RegistryEvent.Register<Block> event) {
		registerFieldsWithAnnotation(event, RegisterBlock.class, RegisterBlock::value, of(BLOCKS));
	}

	private void registerFluids(final RegistryEvent.Register<Fluid> event) {
		registerFieldsWithAnnotation(event, RegisterFluid.class, RegisterFluid::value, of(FLUIDS));
	}

	private void registerEffects(final RegistryEvent.Register<Effect> event) {
		registerFieldsWithAnnotation(event, RegisterEffect.class, RegisterEffect::value, of(EFFECTS));
	}

	private void registerSoundEvents(final RegistryEvent.Register<SoundEvent> event) {
		registerFieldsWithAnnotation(event, RegisterSoundEvent.class, RegisterSoundEvent::value, of(SOUND_EVENTS));
	}

	private void registerTileEntityTypes(final RegistryEvent.Register<TileEntityType<?>> event) {
		registerFieldsWithAnnotation(event, RegisterTileEntityType.class, RegisterTileEntityType::value,
				Optional.of(MatyLibRegistries.TILE_ENTITY_TYPES));
	}

	private void registerContainerTypes(final RegistryEvent.Register<ContainerType<?>> event) {
		registerFieldsWithAnnotation(event, RegisterContainerType.class, RegisterContainerType::value,
				of(CONTAINER_TYPES));
	}

	private void registerParticleTypes(final RegistryEvent.Register<ParticleType<?>> event) {
		registerFieldsWithAnnotation(event, RegisterParticleType.class, RegisterParticleType::value,
				of(PARTICLE_TYPES));
	}

	private void registerRecipeTypes(final RegistryEvent.Register<IRecipeSerializer<?>> event) {
		ReflectionHelper.getFieldsAnnotatedWith(Lists.newArrayList(registryClasses.values()), RegisterRecipeType.class)
				.forEach(field -> {
					if (!field.isAccessible()) { return; }
					try {
						if (field.get(field.getDeclaringClass()) instanceof IRecipeType<?>) {
							IRecipeType<?> type = (IRecipeType<?>) field.get(field.getDeclaringClass());
							Registry.register(Registry.RECIPE_TYPE,
									new ResourceLocation(
											field.getDeclaringClass().getAnnotation(RegistryHolder.class).modid(),
											field.getAnnotation(RegisterRecipeType.class).value()),
									type);
							String modid = field.getDeclaringClass().getAnnotation(RegistryHolder.class).modid();
							if (RECIPE_TYPES.containsKey(modid)) {
								List<IRecipeType<?>> oldList = RECIPE_TYPES.get(modid);
								oldList.add(type);
								RECIPE_TYPES.put(modid, oldList);
							} else {
								RECIPE_TYPES.put(modid, Lists.newArrayList(type));
							}
						} else {
				//@formatter:off
					throw new ProcessingException("The field " + field + " is annotated with @RegisterRecipeType but it is not a recipe type!");
					//@formatter:on
						}
					} catch (IllegalArgumentException | IllegalAccessException | SecurityException e) {
						throw new ProcessingException("Registry Annotations Failed!", e);
					}
				});

		registerFieldsWithAnnotation(event, RegisterRecipeSerializer.class, RegisterRecipeSerializer::value,
				of(RECIPE_SERIALIZERS));
	}

	private <T extends IForgeRegistryEntry<T>, A extends Annotation> void registerFieldsWithAnnotation(
			final RegistryEvent.Register<T> event, Class<A> annotation, Function<A, String> registryName,
			Optional<Map<String, List<T>>> outputMap) {
		AnnotationProcessor.registerFieldsWithAnnotation(Lists.newArrayList(registryClasses.values()), event,
				annotation, (clazz, fieldAn, obj) -> new ResourceLocation(getModID(clazz), registryName.apply(fieldAn)),
				outputMap);
	}

	private void registerCustomRegistries(final RegistryEvent.NewRegistry event) {
		ReflectionHelper
				.getMethodsAnnotatedWith(Lists.newArrayList(registryClasses.values()), RegisterCustomRegistry.class)
				.forEach(method -> {
					try {
						method.invoke(method.getDeclaringClass(), event);
					} catch (IllegalAccessException | InvocationTargetException e) {
						throw new ProcessingException(e);
					} catch (IllegalArgumentException e) {
				//@formatter:off
				throw new ProcessingException("The method " + method + " is annotated with @RegisterCustomRegistry but it cannot be invoked using only RegistryEvent.NewRegistry as a parameter");
				//@formatter:on
					}
				});
	}

	/**
	 * Handles registry annotations
	 *
	 * @param <T>          the type of the object being registered
	 * @param <A>          the class of the registry annotation
	 * @param event        the event in which the objects should be registered
	 * @param annotation   the annotation to look for
	 * @param registryName a {@link TriFunction} containing: the
	 *                     {@link RegistryHolder} annotation of the class of the
	 *                     field being registered, the registry annotation of the
	 *                     field and the object (of type <strong>T</strong>) the
	 *                     field contains. This function will return the registry
	 *                     name of the object, based off the inputed data
	 * @param outputMap    optionally, a map in which the processed objects will be
	 *                     put, as following: <br>
	 *                     A {@link List} with the generic type {@code <T>} will be
	 *                     put as the value corresponding to the key which is the
	 *                     namespace (mod id) of the object's registry name.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IForgeRegistryEntry<T>, A extends Annotation> void registerFieldsWithAnnotation(
			ArrayList<Class<?>> registryClasses, final RegistryEvent.Register<T> event, Class<A> annotation,
			TriFunction<Class<?>, A, T, ResourceLocation> registryName, Optional<Map<String, List<T>>> outputMap) {
		Class<T> objectClass = event.getRegistry().getRegistrySuperType();
		ReflectionHelper.getFieldsAnnotatedWith(registryClasses, annotation).forEach(field -> {
			field.setAccessible(true);
			try {
				AtomicReference<T> registry = new AtomicReference<>(null);
				boolean isGood = false;
				boolean isSupplier = false;
				Object fieldObject = field.get(field.getDeclaringClass());
				if (objectClass.isInstance(fieldObject)) {
					registry.set((T) field.get(field.getDeclaringClass()));
					isGood = true;
				} else if (fieldObject instanceof BetterRegistryObject<?>) {
					BetterRegistryObject<?> regObj = (BetterRegistryObject<?>) fieldObject;
					Method getDecValueMet = regObj.getClass().getDeclaredMethod("getDeclaredValue", new Class<?>[] {});
					getDecValueMet.setAccessible(true);
					Supplier<?> declaredValue = (Supplier<?>) getDecValueMet.invoke(regObj, new Object[] {});
					if (objectClass.isInstance(declaredValue.get())) {
						registry.set((T) declaredValue.get());
						isGood = true;
						isSupplier = true;
					}
				}
				if (isGood && registry.get() != null) {
					ResourceLocation name = registryName.apply(field.getDeclaringClass(),
							field.getAnnotation(annotation), registry.get());
					registry.get().setRegistryName(name);
					event.getRegistry().register(registry.get());
					outputMap.ifPresent(output -> {
						String modid = getModID(field.getDeclaringClass());
						output.computeIfAbsent(modid, key -> Lists.newArrayList()).add(registry.get());
					});
					if (isSupplier) {
						BetterRegistryObject<?> regObj = (BetterRegistryObject<?>) fieldObject;
						Method setNameMethod = regObj.getClass().getDeclaredMethod("setName", ResourceLocation.class);
						setNameMethod.setAccessible(true);
						setNameMethod.invoke(regObj, name);
					}
				} else {
					//@formatter:off
					throw new ProcessingException("The field " + field + " is annotated with " + annotation + " but it is not a " + objectClass);
					//@formatter:on
				}
			} catch (IllegalArgumentException | IllegalAccessException | InvocationTargetException
					| NoSuchMethodException | SecurityException e) {
				throw new ProcessingException("Registry Annotations Failed!", e);
			}
		});
	}

	public static String getModID(Class<?> clazz) {
		if (clazz.isAnnotationPresent(RegistryHolder.class)) {
			return clazz.getAnnotation(RegistryHolder.class).modid();
		} else if (clazz.isAnnotationPresent(Module.class)) { return clazz.getAnnotation(Module.class).id().modid(); }
		return null;
	}

	public static class ProcessingException extends RuntimeException {

		private static final long serialVersionUID = 1688668579640237515L;

		public ProcessingException() {
			super();
		}

		public ProcessingException(String message) {
			super(message);
		}

		public ProcessingException(String message, Throwable cause) {
			super(message, cause);
		}

		public ProcessingException(Throwable cause) {
			super(cause);
		}
	}

}
