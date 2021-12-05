package com.matyrobbrt.lib.compat.top;

import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.matyrobbrt.lib.MatyLib;
import com.matyrobbrt.lib.util.ModIDs;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;
import net.minecraftforge.fml.InterModComms;

public class TheOneProbeCompat {

	public static final Logger LOGGER = LogManager.getLogger();

	private static boolean registered;

	public static void register() {
		if (registered) { return; }
		registered = true;
		InterModComms.sendTo(ModIDs.THE_ONE_PROBE, "getTheOneProbe", () -> new Function<ITheOneProbe, Void>() {

			@Override
			public Void apply(ITheOneProbe theOneProbe) {
				LOGGER.info("MatyLib: Found The One Probe! Enabled support!");
				theOneProbe.registerProvider(new Driver());
				return null;
			}
		});
	}

	public static class Driver implements IProbeInfoProvider {

		@Override
		public String getID() { return MatyLib.INSTANCE.rl("default_driver").toString(); }

		@Override
		public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, PlayerEntity player, World world,
				BlockState blockState, IProbeHitData data) {
			Block block = blockState.getBlock();
			if (block instanceof ITOPInfoProvider) {
				ITOPInfoProvider provider = (ITOPInfoProvider) block;
				ITOPDriver driver = provider.getTheOneProbeDriver();
				if (driver != null) {
					driver.addProbeInfo(mode, probeInfo, player, world, blockState, data);
				}
			}

		}
	}

}
