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
import com.matyrobbrt.lib.network.INetworkMessage;
import com.matyrobbrt.lib.tile_entity.BaseTileEntity;

import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.server.ServerWorld;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class SyncValuesMessage implements INetworkMessage {

	public enum Direction {
		SERVER_TO_CLIENT, CLIENT_TO_SERVER;
	}

	public final BlockPos pos;
	public final CompoundNBT nbt;
	public final Direction direction;

	public SyncValuesMessage(BlockPos pos, BaseTileEntity te, Direction direction) {
		this.pos = pos;
		this.nbt = SyncValue.Helper.writeSyncValues(te.getSyncFields(), te, te.save(new CompoundNBT()),
				SyncValue.SyncType.PACKET);
		this.direction = direction;
	}

	public SyncValuesMessage(BlockPos pos, CompoundNBT nbt, Direction direction) {
		this.pos = pos;
		this.nbt = nbt;
		this.direction = direction;
	}

	@Override
	public void encode(PacketBuffer buffer) {
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
			context.enqueueWork(
					() -> DistExecutor.unsafeRunWhenOn(Dist.DEDICATED_SERVER, () -> () -> handleServer(this, context)));
		}
	}

	@SuppressWarnings("resource")
	private static void handleClient(SyncValuesMessage syncValuesMessage, Context context) {
		ClientWorld client = Minecraft.getInstance().level;
		client.getBlockEntity(syncValuesMessage.pos).load(client.getBlockState(syncValuesMessage.pos),
				syncValuesMessage.nbt);
	}

	private static void handleServer(SyncValuesMessage syncValuesMessage, Context context) {
		ServerWorld server = context.getSender().getLevel();
		server.getBlockEntity(syncValuesMessage.pos).load(server.getBlockState(syncValuesMessage.pos),
				syncValuesMessage.nbt);
	}

	public static SyncValuesMessage decode(PacketBuffer buffer) {
		return new SyncValuesMessage(buffer.readBlockPos(), buffer.readNbt(), buffer.readEnum(Direction.class));
	}

}
