package com.matyrobbrt.lib.mixin;

import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.resources.ResourcePackType;

import net.minecraftforge.common.IExtensibleEnum;

@Mixin(ResourcePackType.class)
public class ResourcePackTypeExtender implements IExtensibleEnum {

	@SuppressWarnings("unused")
	private static ResourcePackType create(String name, String directoryName) {
		throw new IllegalStateException("Enum not extended");
	}

}
