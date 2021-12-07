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
