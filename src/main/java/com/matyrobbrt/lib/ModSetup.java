package com.matyrobbrt.lib;

import com.matyrobbrt.lib.registry.annotation.AnnotationProcessor;

import net.minecraft.util.ResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

/**
 * Base class for mod main classes
 * 
 * @author matyrobbrt
 *
 */
public abstract class ModSetup {

	public final IEventBus modBus;
	public final IEventBus forgeBus;

	protected final String modId;

	/**
	 * @param modid needed to that classes that inherit this one <b>have to</b> call
	 *              super
	 */
	protected ModSetup(String modid) {
		this.modId = modid;
		modBus = FMLJavaModLoadingContext.get().getModEventBus();
		modBus.addListener(this::onCommonSetup);
		modBus.addListener(this::onInterModEnqueue);

		if (clientSetup() != null) {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::clientSetup);
		}

		if (annotationProcessor() != null) {
			annotationProcessor().register(modBus);
		}

		forgeBus = MinecraftForge.EVENT_BUS;
		forgeBus.register(this);
	}

	public ResourceLocation rl(String name) {
		return new ResourceLocation(modId, name);
	}

	public ClientSetup clientSetup() {
		return null;
	}

	public AnnotationProcessor annotationProcessor() {
		return new AnnotationProcessor(modId);
	}

	public void onCommonSetup(final FMLCommonSetupEvent event) {
	}

	public void onInterModEnqueue(final InterModEnqueueEvent event) {

	}
}
