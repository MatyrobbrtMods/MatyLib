package com.matyrobbrt.lib.tile_entity;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.google.common.collect.Lists;
import com.matyrobbrt.lib.annotation.SyncValue;
import com.matyrobbrt.lib.network.BaseNetwork;
import com.matyrobbrt.lib.network.matylib.MatyLibNetwork;
import com.matyrobbrt.lib.network.matylib.SyncValuesMessage;
import com.matyrobbrt.lib.network.matylib.SyncValuesMessage.Direction;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class BaseTileEntity extends TileEntity {

	public BaseTileEntity(TileEntityType<?> type) {
		super(type);
	}

	@Override
	public void load(BlockState state, CompoundNBT nbt) {
		super.load(state, nbt);
		SyncValue.Helper.readSyncValues(getSyncFields(), this, nbt);
	}

	@Override
	public CompoundNBT save(CompoundNBT tags) {
		CompoundNBT compoundTag = super.save(tags);
		SyncValue.Helper.writeSyncValues(getSyncFields(), this, compoundTag, SyncValue.SyncType.WRITE);
		return compoundTag;
	}

	@Override
	public CompoundNBT getUpdateTag() {
		CompoundNBT nbt = super.getUpdateTag();
		SyncValue.Helper.writeSyncValues(getSyncFields(), this, nbt, SyncValue.SyncType.PACKET);
		return nbt;
	}

	/**
	 * Call this method in order to sync {@link SyncValue.SyncType#PACKET} values
	 * 
	 * @param direction
	 */
	public void sync(Direction direction) {
		BaseNetwork.sendToAllTracking(MatyLibNetwork.CHANNEL, new SyncValuesMessage(worldPosition, this, direction),
				this);
	}

	private List<Field> fields = null;

	public Field[] getSyncFields() {
		if (fields == null) {
			createSyncFields();
		}
		return fields.toArray(new Field[] {});
	}

	private void createSyncFields() {
		fields = Lists.newArrayList(FieldUtils.getFieldsListWithAnnotation(getClass(), SyncValue.class));
	}
}
