package com.matyrobbrt.lib.registry.deferred;

import net.minecraft.util.ResourceLocation;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;

public abstract class DeferredRegisterWrapper<T extends IForgeRegistryEntry<T>> {

	/** {@link DeferredRegister} instance, use this to provide register methods */
	protected final DeferredRegister<T> register;

	/** Mod ID for registration */
	private final String modID;

	protected DeferredRegisterWrapper(IForgeRegistry<T> reg, String modID) {
		register = DeferredRegister.create(reg, modID);
		this.modID = modID;
	}

	public void register(IEventBus modBus) {
		register.register(modBus);
	}

	protected ResourceLocation resource(String name) {
		return new ResourceLocation(modID, name);
	}

}
