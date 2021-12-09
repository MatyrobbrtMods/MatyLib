package com.matyrobbrt.lib.wrench;

import net.minecraftforge.common.IExtensibleEnum;

/**
 * <strong> AS THIS ENUM IMPLEMENTS {@link IExtensibleEnum} DO NOT USE SWITCHES
 * FOR IT! </strong>
 * 
 * @author matyrobbrt
 *
 */
public enum WrenchMode implements IExtensibleEnum {

	DISMANTALE, CONFIGURE;

	public static WrenchMode create(String name) {
		throw new IllegalAccessError("Enum not extended!");
	}
}
