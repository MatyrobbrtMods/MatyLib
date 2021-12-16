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

package com.matyrobbrt.lib.network;

import java.util.Optional;
import java.util.function.Function;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

/**
 * A base class for networks
 * 
 * @author matyrobbrt
 *
 */
public abstract class BaseNetwork {
	
	protected static int ID = 0;

	protected static int nextId() {
		return ID++;
	}

	protected static <M extends INetworkMessage> void registerClientToServer(SimpleChannel channel, Class<M> type,
			Function<FriendlyByteBuf, M> decoder) {
		registerMessage(channel, type, decoder, NetworkDirection.PLAY_TO_SERVER);
	}

	protected static <M extends INetworkMessage> void registerServerToClient(SimpleChannel channel, Class<M> type,
			Function<FriendlyByteBuf, M> decoder) {
		registerMessage(channel, type, decoder, NetworkDirection.PLAY_TO_CLIENT);
	}

	private static <M extends INetworkMessage> void registerMessage(SimpleChannel channel, Class<M> msgClass,
			Function<FriendlyByteBuf, M> decoder, NetworkDirection direction) {
		channel.registerMessage(nextId(), msgClass, INetworkMessage::encode, decoder, INetworkMessage::handle,
				Optional.of(direction));
	}

	public static <MSG> void sendToAllTracking(SimpleChannel channel, MSG message, BlockEntity tile) {
		sendToAllTracking(channel, message, tile.getLevel(), tile.getBlockPos());
	}

	@SuppressWarnings("resource")
	public static <MSG> void sendToAllTracking(SimpleChannel channel, MSG message, Level world, BlockPos pos) {
		if (world instanceof ServerLevel) {
			((ServerLevel) world).getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false)
					.forEach(p -> sendTo(channel, message, p));
		} else {
			channel.send(PacketDistributor.TRACKING_CHUNK.with(() -> world.getChunk(pos.getX() >> 4, pos.getZ() >> 4)),
					message);
		}
	}

	public static <MSG> void sendToAllInWorld(SimpleChannel channel, MSG message, Level world) {
		if (world instanceof ServerLevel) {
			((ServerLevel) world).getPlayers(player -> (player.level == world))
					.forEach(p -> sendTo(channel, message, p));
		} else {
			channel.send(PacketDistributor.ALL.noArg(), message);
		}
	}

	public static <MSG> void sendToAll(SimpleChannel channel, MSG message) {
		channel.send(PacketDistributor.ALL.noArg(), message);
	}

	/**
	 * Send this message to the specified player.
	 *
	 * @param message - the message to send
	 * @param player  - the player to send it to
	 */
	public static <MSG> void sendTo(SimpleChannel channel, MSG message, ServerPlayer player) {
		channel.sendTo(message, player.connection.getConnection(), NetworkDirection.PLAY_TO_CLIENT);
	}

}
