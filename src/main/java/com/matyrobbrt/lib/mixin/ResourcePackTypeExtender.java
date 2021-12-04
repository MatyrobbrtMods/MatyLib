package com.matyrobbrt.lib.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.resources.ResourcePackType;

import net.minecraftforge.common.IExtensibleEnum;

@Mixin(value = ResourcePackType.class, priority = 1200)
public class ResourcePackTypeExtender implements IExtensibleEnum {

	@SuppressWarnings("unused")
	private static ResourcePackType create(String name, String directoryName) {
		throw new IllegalStateException("Enum not extended");
	}

}
