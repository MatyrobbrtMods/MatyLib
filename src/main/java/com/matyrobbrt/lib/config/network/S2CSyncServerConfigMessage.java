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

package com.matyrobbrt.lib.config.network;

import java.io.ByteArrayInputStream;

import com.matyrobbrt.lib.config.ConfigHolder;
import com.matyrobbrt.lib.config.ConfigManager;
import com.matyrobbrt.lib.config.IConfigData;
import com.matyrobbrt.lib.config.serializer.IConfigSerializer.SerializationException;
import com.matyrobbrt.lib.network.INetworkMessage;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class S2CSyncServerConfigMessage implements INetworkMessage {

	private final ResourceLocation name;
	private final byte[] data;

	public S2CSyncServerConfigMessage(ResourceLocation name, byte[] data) {
		this.name = name;
		this.data = data;
	}

	@Override
	public void handle(Context context) {
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleClient(this, context));
	}

	private static <C extends IConfigData> void handleClient(S2CSyncServerConfigMessage msg, Context context) {
		new Thread(() -> {
			ConfigHolder<C> holder = ConfigManager.getConfigHolder(msg.name);
			try {
				holder.setConfig(holder.getSerializer().deserialize(new ByteArrayInputStream(msg.data)));
			} catch (SerializationException e) {
				throw new IllegalArgumentException("Exception while transfering server config to clients!", e);
			}
		}).start();
	}

	@Override
	public void encode(PacketBuffer buffer) {
		buffer.writeResourceLocation(name);
		buffer.writeByteArray(data);
	}

	public static S2CSyncServerConfigMessage decode(PacketBuffer buffer) {
		ResourceLocation name = buffer.readResourceLocation();
		byte[] data = buffer.readByteArray();
		return new S2CSyncServerConfigMessage(name, data);
	}
}
