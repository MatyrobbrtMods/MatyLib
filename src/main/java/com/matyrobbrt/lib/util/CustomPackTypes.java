package com.matyrobbrt.lib.util;

import java.lang.reflect.Method;

import net.minecraft.resources.ResourcePackType;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

public class CustomPackTypes {

	public static final Method CREATE = ObfuscationReflectionHelper.findMethod(ResourcePackType.class, "create",
			String.class, String.class);

}
