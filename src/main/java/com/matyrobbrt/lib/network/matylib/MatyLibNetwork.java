/**
 * This file is part of the MatyLib Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2021 Matyrobbrt
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.matyrobbrt.lib.network.matylib;

import com.matyrobbrt.lib.MatyLib;
import com.matyrobbrt.lib.config.network.S2CSyncServerConfigMessage;
import com.matyrobbrt.lib.network.BaseNetwork;

import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class MatyLibNetwork extends BaseNetwork {

	public static final String NETWORK_VERSION = "0.1.0";

	public static final SimpleChannel MAIN_CHANNEL = newSimpleChannel("main");
	public static final SimpleChannel CONFIG_CHANNEL = newSimpleChannel("config");

	public static void init() {
		registerServerToClient(MAIN_CHANNEL, SyncValuesMessage.class, SyncValuesMessage::decode);
		registerClientToServer(MAIN_CHANNEL, SyncValuesMessage.class, SyncValuesMessage::decode);

		registerServerToClient(CONFIG_CHANNEL, S2CSyncServerConfigMessage.class, S2CSyncServerConfigMessage::decode);
	}

	private static SimpleChannel newSimpleChannel(String name) {
		return NetworkRegistry.newSimpleChannel(MatyLib.INSTANCE.rl(name), () -> NETWORK_VERSION,
				version -> version.equals(NETWORK_VERSION), version -> version.equals(NETWORK_VERSION));
	}

}
