package com.matyrobbrt.lib.compat.top;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import mcjty.theoneprobe.TheOneProbe;
import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.ProbeMode;

/**
 * A base interface for {@link TheOneProbe} info providers. In order to avoid
 * {@link ClassNotFoundException}s any class inheriting this should be
 * independent!
 * 
 * @author matyrobbrt
 *
 */
public interface ITOPDriver {

	/**
	 * Adds info to the probe
	 * 
	 * @param probeMode
	 * @param probeInfo
	 * @param player
	 * @param level
	 * @param blockState
	 * @param probeData
	 */
	void addProbeInfo(ProbeMode probeMode, IProbeInfo probeInfo, PlayerEntity player, World level, BlockState blockState,
			IProbeHitData probeData);

}
