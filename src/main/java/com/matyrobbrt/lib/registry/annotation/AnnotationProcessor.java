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
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import net.minecraftforge.registries.NewRegistryEvent;
import org.objectweb.asm.Type;

import com.google.common.collect.Lists;
import com.matyrobbrt.lib.annotation.RL;
import com.matyrobbrt.lib.module.IModule;
import com.matyrobbrt.lib.module.Module;
import com.matyrobbrt.lib.registry.BetterRegistryObject;
import com.matyrobbrt.lib.registry.MatyLibRegistries;
import com.matyrobbrt.lib.registry.annotation.recipe.RegisterRecipeSerializer;
import com.matyrobbrt.lib.registry.annotation.recipe.RegisterRecipeType;
import com.matyrobbrt.lib.util.LockableList;
import com.matyrobbrt.lib.util.ReflectionHelper;
import com.matyrobbrt.lib.util.TriFunction;
import com.matyrobbrt.lib.util.helper.TernaryHelper;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.GenericEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * Handles all of the RegistryAnnotations (all annotations included in this
 * package {@link com.matyrobbrt.lib.registry.annotation}), and {@link Module}
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

	protected final LockableList<Class<?>> ignoredRegistryTypes = new LockableList<>();

	protected Function<Block, CreativeModeTab> autoBlockItemTab = block -> CreativeModeTab.TAB_MISC;

	protected final HashMap<Class<?>, String> registryClasses = new HashMap<>();

	protected final HashMap<Class<?>, String> moduleClasses = new HashMap<>();
	protected final HashMap<ResourceLocation, IModule> modules = new HashMap<>();

	protected Runnable afterInit = () -> {};

	/**
	 * Creates a new {@link AnnotationProcessor} which will be used in order to
	 * process annotations. It is recommended to store this statically somewhere.
	 *
	 * @param modid the mod id to process the annotations for
	 */
	public AnnotationProcessor(String modid) {
		ownerModID = modid;
	}

	public void setAutoBlockItemTab(Function<Block, CreativeModeTab> tab) { this.autoBlockItemTab = tab; }

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
	 * Ignores a registry type from being processed
	 * 
	 * @param registryType
	 * @since 1.0.4
	 */
	public void ignoreRegistryType(Class<?> registryType) {
		ignoredRegistryTypes.add(registryType);
	}

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
		ignoredRegistryTypes.setLocked(cls -> true);

		// Registry Annotations
		addListenerIfNotIgnored(Block.class, this::registerBlocks);
		addListenerIfNotIgnored(MenuType.class, this::registerMenuTypes);
		addListenerIfNotIgnored(MobEffect.class, this::registerEffects);
		addListenerIfNotIgnored(Potion.class, this::registerPotionTypes);
		addListenerIfNotIgnored(Fluid.class, this::registerFluids);
		addListenerIfNotIgnored(Item.class, this::registerItems);
		addListenerIfNotIgnored(ParticleType.class, this::registerParticleTypes);
		addListenerIfNotIgnored(SoundEvent.class, this::registerSoundEvents);
		addListenerIfNotIgnored(Attribute.class, this::registerAttributes);
		addListenerIfNotIgnored(RecipeSerializer.class, this::registerRecipeTypes);
		addListenerIfNotIgnored(BlockEntityType.class, this::registerTileEntityTypes);
		addListenerIfNotIgnored(EntityType.class, this::registerEntityTypes);

		modBus.addListener(this::registerCustomRegistries);
	}

	protected final <T extends GenericEvent<? extends F>, F> void addListenerIfNotIgnored(Class<F> clazz,
			Consumer<T> consumer) {
		if (!ignoredRegistryTypes.contains(clazz)) {
			modBus.addGenericListener(clazz, consumer);
		}
	}

	public void addRegistryClass(Class<?> clazz) {
		registryClasses.put(clazz, ownerModID);
	}

	public void addModule(IModule module, ResourceLocation name) {
		modules.put(name, module);
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
				.filter(a -> a.annotationType().equals(Type.getType(RegistryHolder.class))).toList();

		regAnnotations.stream().filter(a -> Type.getType(RegistryHolder.class).equals(a.annotationType()))
				.filter(a -> a.targetType() == ElementType.TYPE).forEach(data -> {
					try {
						Class<?> clazz = Class.forName(data.clazz().getClassName(), false, getClass().getClassLoader());
						if (clazz.getAnnotation(RegistryHolder.class).modid().equals(ownerModID)
								&& clazz.getAnnotation(RegistryHolder.class).enabled()) {
							if (!registryClasses.containsKey(clazz)) {
								registryClasses.put(clazz, clazz.getAnnotation(RegistryHolder.class).modid());
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
				.filter(a -> a.annotationType().equals(Type.getType(Module.class))).toList();

		moduleAnnotations.stream().filter(a -> Type.getType(Module.class).equals(a.annotationType()))
				.filter(a -> a.targetType() == ElementType.TYPE).forEach(data -> {
					try {
						Class<?> clazz = Class.forName(data.clazz().getClassName(), false, getClass().getClassLoader());
						RL id = clazz.getAnnotation(Module.class).id();
						if (id.modid().equals(ownerModID)) {
							moduleClasses.put(clazz, id.modid());
							registryClasses.put(clazz, id.modid());
							Constructor<?> constructor = clazz.getConstructor();
							constructor.setAccessible(true);
							Object instance = constructor.newInstance();
							if (instance instanceof IModule module) {
								ResourceLocation name = new ResourceLocation(id.modid(), id.path());
								if (!modules.containsKey(name)) {
									modules.put(name, module);
								} else {
									throw new ProcessingException("Duplicate module id " + name);
								}
							} else {
								throw new ProcessingException(
										"The module " + clazz + " is not an instace of " + IModule.class);
							}
						}
					} catch (IllegalAccessError | InstantiationException | IllegalAccessException
							| NoSuchMethodException | InvocationTargetException | SecurityException e) {
						throw new ProcessingException(e);
					} catch (ClassNotFoundException e) {
						throw new ProcessingException("A class which is a module was not found!", e);
					}
				});

	}

	public void unregisterRegistryClass(Class<?> clazz) {
		if (registryClasses.containsKey(clazz)) {
			registryClasses.remove(clazz);
		}
	}

	public void unregisterModule(Class<?> clazz) {
		ResourceLocation name = clazz.isAnnotationPresent(Module.class) ? TernaryHelper.supplier(() -> {
			RL rl = clazz.getAnnotation(Module.class).id();
			return new ResourceLocation(rl.modid(), rl.path());
		}) : null;
		if (name != null) {
			moduleClasses.remove(clazz);
			modules.remove(name);
		}
	}

	// Registry Annotations

	private void registerItems(final RegistryEvent.Register<Item> event) {
		registerFieldsWithAnnotation(event, RegisterItem.class, RegisterItem::value, of(ITEMS));
		registerFieldsWithAnnotation(Lists.newArrayList(registryClasses.keySet()), event, RegisterBlockItem.class,
				(classAn, fieldAn, obj) -> {
					if (obj instanceof BlockItem blockItem) { return blockItem.getBlock().getRegistryName(); }
					throw new ProcessingException("Invalid BlockItem");
				}, Optional.empty());
		registerAutoBIs(event);
	}

	private void registerAutoBIs(final RegistryEvent.Register<Item> event) {
		ReflectionHelper.getFieldsAnnotatedWith(Lists.newArrayList(registryClasses.keySet()), AutoBlockItem.class)
				.forEach(field -> {
					try {
						Object value = field.get(field.getDeclaringClass());
						if (value instanceof Block block) {
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

	private void registerEffects(final RegistryEvent.Register<MobEffect> event) {
		registerFieldsWithAnnotation(event, RegisterMobEffect.class, RegisterMobEffect::value, of(MOB_EFFECTS));
	}

	private void registerPotionTypes(final RegistryEvent.Register<Potion> event) {
		registerFieldsWithAnnotation(event, RegisterPotion.class, RegisterPotion::value, of(POTION_TYPES));
	}

	private void registerSoundEvents(final RegistryEvent.Register<SoundEvent> event) {
		registerFieldsWithAnnotation(event, RegisterSoundEvent.class, RegisterSoundEvent::value, of(SOUND_EVENTS));
	}

	private void registerAttributes(final RegistryEvent.Register<Attribute> event) {
		registerFieldsWithAnnotation(event, RegisterAttribute.class, RegisterAttribute::value, of(ATTRIBUTES));
	}

	private void registerEntityTypes(final RegistryEvent.Register<EntityType<?>> event) {
		registerFieldsWithAnnotation(event, RegisterEntityType.class, RegisterEntityType::value,
				Optional.of(ENTITY_TYPES));
	}

	private void registerTileEntityTypes(final RegistryEvent.Register<BlockEntityType<?>> event) {
		registerFieldsWithAnnotation(event, RegisterBlockEntityType.class, RegisterBlockEntityType::value,
				Optional.of(BLOCK_ENTITY_TYPES));
	}

	private void registerMenuTypes(final RegistryEvent.Register<MenuType<?>> event) {
		registerFieldsWithAnnotation(event, RegisterMenuType.class, RegisterMenuType::value, of(MENU_TYPES));
	}

	private void registerParticleTypes(final RegistryEvent.Register<ParticleType<?>> event) {
		registerFieldsWithAnnotation(event, RegisterParticleType.class, RegisterParticleType::value,
				of(PARTICLE_TYPES));
	}

	private void registerRecipeTypes(final RegistryEvent.Register<RecipeSerializer<?>> event) {
		ReflectionHelper.getFieldsAnnotatedWith(Lists.newArrayList(registryClasses.keySet()), RegisterRecipeType.class)
				.forEach(field -> {
					field.setAccessible(true);
					try {
						if (field.get(field.getDeclaringClass())instanceof RecipeType<?> type) {
							Registry.register(Registry.RECIPE_TYPE,
									new ResourceLocation(getModID(field.getDeclaringClass()),
											field.getAnnotation(RegisterRecipeType.class).value()),
									type);
							String modid = getModID(field.getDeclaringClass());
							if (RECIPE_TYPES.containsKey(modid)) {
								List<RecipeType<?>> oldList = RECIPE_TYPES.get(modid);
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

	protected <T extends IForgeRegistryEntry<T>, A extends Annotation> void registerFieldsWithAnnotation(
			final RegistryEvent.Register<T> event, Class<A> annotation, Function<A, String> registryName,
			Optional<Map<String, List<T>>> outputMap) {
		registerFieldsWithAnnotation(
				Lists.newArrayList(registryClasses.keySet()), event, annotation, (clazz, fieldAn,
						obj) -> new ResourceLocation(getModIDBasedOnAnnotations(clazz), registryName.apply(fieldAn)),
				outputMap);
	}

	private void registerCustomRegistries(final NewRegistryEvent event) {
		ReflectionHelper
				.getMethodsAnnotatedWith(Lists.newArrayList(registryClasses.keySet()), RegisterCustomRegistry.class)
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

	public <T extends IForgeRegistryEntry<T>, A extends Annotation> void registerFieldsWithAnnotation(
			ArrayList<Class<?>> registryClasses, final RegistryEvent.Register<T> event, Class<A> annotation,
			TriFunction<Class<?>, A, T, ResourceLocation> registryName, Optional<Map<String, List<T>>> outputMap) {
		registerFieldsWithAnnotation(registryClasses, event, annotation, registryName, this::getModID, outputMap);
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
	 * @param modid        a function that figures out the mod id, based on the
	 *                     given class
	 * @param outputMap    optionally, a map in which the processed objects will be
	 *                     put, as following: <br>
	 *                     A {@link List} with the generic type {@code <T>} will be
	 *                     put as the value corresponding to the key which is the
	 *                     namespace (mod id) of the object's registry name.
	 */
	@SuppressWarnings("unchecked")
	public static <T extends IForgeRegistryEntry<T>, A extends Annotation> void registerFieldsWithAnnotation(
			ArrayList<Class<?>> registryClasses, final RegistryEvent.Register<T> event, Class<A> annotation,
			TriFunction<Class<?>, A, T, ResourceLocation> registryName, Function<Class<?>, String> modid,
			Optional<Map<String, List<T>>> outputMap) {
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
				} else if (fieldObject instanceof BetterRegistryObject<?> regObj) {
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
					outputMap.ifPresent(output -> output
							.computeIfAbsent(modid.apply(field.getDeclaringClass()), key -> Lists.newArrayList())
							.add(registry.get()));
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

	public static String getModIDBasedOnAnnotations(Class<?> clazz) {
		if (clazz.isAnnotationPresent(RegistryHolder.class)) {
			return clazz.getAnnotation(RegistryHolder.class).modid();
		} else if (clazz.isAnnotationPresent(Module.class)) { return clazz.getAnnotation(Module.class).id().modid(); }
		return null;
	}

	protected String getModID(Class<?> clazz) {
		if (registryClasses.containsKey(clazz)) {
			return registryClasses.get(clazz);
		} else if (moduleClasses.containsKey(clazz)) {
			return moduleClasses.get(clazz);
		} else if (clazz.isAnnotationPresent(RegistryHolder.class)) {
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
