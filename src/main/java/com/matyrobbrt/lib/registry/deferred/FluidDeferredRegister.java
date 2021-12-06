package com.matyrobbrt.lib.registry.deferred;

import java.util.function.Supplier;

import net.minecraft.block.Block;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FluidDeferredRegister extends DeferredRegisterWrapper<Fluid> {

	protected final DeferredRegister<Block> blocks;
	protected final DeferredRegister<Item> items;

	protected FluidDeferredRegister(String modID) {
		super(ForgeRegistries.FLUIDS, modID);
		blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, modID);
		items = DeferredRegister.create(ForgeRegistries.ITEMS, modID);
	}

	@Override
	public void register(IEventBus modBus) {
		super.register(modBus);
		blocks.register(modBus);
		items.register(modBus);
	}

	public <T extends Item> RegistryObject<T> registerItem(String name, Supplier<? extends T> sup) {
		return items.register(name, sup);
	}

	public <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<? extends T> sup) {
		return blocks.register(name, sup);
	}

	public <T extends Fluid> RegistryObject<T> registerFluid(String name, Supplier<? extends T> sup) {
		return register.register(name, sup);
	}
}
