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

import com.matyrobbrt.lib.annotation.SyncValue;
import com.matyrobbrt.lib.network.BaseNetwork;
import com.matyrobbrt.lib.network.INetworkMessage;
import com.matyrobbrt.lib.tile_entity.BaseTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent.Context;

public class SyncValuesMessage implements INetworkMessage {

	public enum Direction {
		SERVER_TO_CLIENT, CLIENT_TO_SERVER;
	}

	public final BlockPos pos;
	public final CompoundTag nbt;
	public final Direction direction;

	public SyncValuesMessage(BlockPos pos, BaseTileEntity te, Direction direction) {
		this.pos = pos;
		this.nbt = SyncValue.Helper.writeSyncValues(te.getSyncFields(), te, te.save(new CompoundTag()),
				SyncValue.SyncType.PACKET);
		this.direction = direction;
	}

	public SyncValuesMessage(BlockPos pos, CompoundTag nbt, Direction direction) {
		this.pos = pos;
		this.nbt = nbt;
		this.direction = direction;
	}

	@Override
	public void encode(FriendlyByteBuf buffer) {
		buffer.writeBlockPos(pos);
		buffer.writeNbt(nbt);
		buffer.writeEnum(direction);
	}

	@Override
	public void handle(Context context) {
		if (direction == Direction.SERVER_TO_CLIENT) {
			context.enqueueWork(
					() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handleClient(this, context)));
		} else if (direction == Direction.CLIENT_TO_SERVER) {
			handleServer(this, context);
		}
	}

	@SuppressWarnings("resource")
	private static void handleClient(SyncValuesMessage syncValuesMessage, Context context) {
		ClientLevel client = Minecraft.getInstance().level;
		if (client == null) { return; }
		BlockEntity tile = client.getBlockEntity(syncValuesMessage.pos);
		if (tile != null && client.isClientSide()) {
			tile.load(syncValuesMessage.nbt);
		}
	}

	@SuppressWarnings("unused")
	private static void handleServer(SyncValuesMessage syncValuesMessage, Context context) {
		ServerLevel server = context.getSender().getLevel();
		if (server == null) { return; }
		BlockEntity tile = server.getBlockEntity(syncValuesMessage.pos);
		if (tile != null && !server.isClientSide()) {
			tile.load(syncValuesMessage.nbt);
		}
	}

	public static SyncValuesMessage decode(FriendlyByteBuf buffer) {
		return new SyncValuesMessage(buffer.readBlockPos(), buffer.readNbt(), buffer.readEnum(Direction.class));
	}

	public static void send(BaseTileEntity tile, Direction direction) {
		if (direction == Direction.SERVER_TO_CLIENT) {
			BaseNetwork.sendToAllTracking(MatyLibNetwork.CHANNEL,
					new SyncValuesMessage(tile.getBlockPos(), tile, direction), tile);
		} else {
			MatyLibNetwork.CHANNEL.sendToServer(new SyncValuesMessage(tile.getBlockPos(), tile, direction));
		}
	}

}
