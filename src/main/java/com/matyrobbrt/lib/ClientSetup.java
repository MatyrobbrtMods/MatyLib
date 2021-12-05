package com.matyrobbrt.lib;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ClientSetup {

	protected final IEventBus modBus;

	public ClientSetup(final IEventBus modBus) {
		this.modBus = modBus;
		modBus.addListener(this::onClientSetup);
	}

	public void onClientSetup(final FMLClientSetupEvent event) {

	}

}
