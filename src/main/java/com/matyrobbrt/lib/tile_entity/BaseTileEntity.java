/**
 * This file is part of the MatyLib Minecraft (Java Edition) mod and is licensed
 * under the MIT license:
 *
 * MIT License
 *
 * Copyright (c) 2022 Matyrobbrt
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

package com.matyrobbrt.lib.tile_entity;

import java.lang.reflect.Field;
import java.util.List;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.google.common.collect.Lists;
import com.matyrobbrt.lib.annotation.SyncValue;
import com.matyrobbrt.lib.network.matylib.SyncValuesMessage;
import com.matyrobbrt.lib.network.matylib.SyncValuesMessage.Direction;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BaseTileEntity extends BlockEntity {

	public BaseTileEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		SyncValue.Helper.readSyncValues(getSyncFields(), this, nbt);
	}
	
	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		SyncValue.Helper.writeSyncValues(getSyncFields(), this, tag, SyncValue.SyncType.WRITE);
	}

	@Override
	public CompoundTag getUpdateTag() {
		CompoundTag nbt = super.getUpdateTag();
		SyncValue.Helper.writeSyncValues(getSyncFields(), this, nbt, SyncValue.SyncType.PACKET);
		return nbt;
	}

	/**
	 * Call this method in order to sync {@link SyncValue.SyncType#PACKET} values
	 * 
	 * @param direction
	 */
	public void sync(Direction direction) {
		SyncValuesMessage.send(this, direction);
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
