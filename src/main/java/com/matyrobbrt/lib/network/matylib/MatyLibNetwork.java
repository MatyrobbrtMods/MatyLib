package com.matyrobbrt.lib.network.matylib;

import com.matyrobbrt.lib.MatyLib;
import com.matyrobbrt.lib.network.BaseNetwork;

import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class MatyLibNetwork extends BaseNetwork {

	public static final String NETWORK_VERSION = "0.1.0";

	public static final SimpleChannel CHANNEL = newSimpleChannel("channel");

	public static void init() {
		registerServerToClient(CHANNEL, SyncValuesMessage.class, SyncValuesMessage::decode);
	}

	private static SimpleChannel newSimpleChannel(String name) {
		return NetworkRegistry.newSimpleChannel(MatyLib.INSTANCE.rl(name), () -> NETWORK_VERSION,
				version -> version.equals(NETWORK_VERSION), version -> version.equals(NETWORK_VERSION));
	}

}
