package com.matyrobbrt.lib.keybind;

import java.util.function.Consumer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;

public class BaseKeyBinding extends KeyBinding {

	public final boolean checkEveryTick;

	private final Consumer<Minecraft> toRun;

	public BaseKeyBinding(String keyName, int key, String category) {
		this(keyName, key, category, mc -> {}, true);
	}

	public BaseKeyBinding(String keyName, int key, String category, Consumer<Minecraft> toRun, boolean checkEveryTick) {
		super(keyName, key, category);
		this.toRun = toRun;
		this.checkEveryTick = checkEveryTick;
		KeyBindingHandler.HANDLED_KEY_BINDINGS.add(this);
	}

	public void run(Minecraft mc) {
		toRun.accept(mc);
	}
}
