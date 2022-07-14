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

package com.matyrobbrt.lib.compat.top;

import java.util.function.Function;

import org.apache.logging.log4j.Logger;

import com.matyrobbrt.lib.MatyLib;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import mcjty.theoneprobe.api.IProbeHitData;
import mcjty.theoneprobe.api.IProbeInfo;
import mcjty.theoneprobe.api.IProbeInfoProvider;
import mcjty.theoneprobe.api.ITheOneProbe;
import mcjty.theoneprobe.api.ProbeMode;

class TOPCompatSetup {

	public static class Create implements Function<ITheOneProbe, Void> {

		private final Logger logger;

		public Create(Logger logger) {
			this.logger = logger;
		}

		@Override
		public Void apply(ITheOneProbe theOneProbe) {
			logger.info("MatyLib: Found The One Probe! Enabled support!");
			theOneProbe.registerProvider(new Driver());
			return null;
		}
	}

	public static class Driver implements IProbeInfoProvider {

		@Override
		public ResourceLocation getID() { return MatyLib.INSTANCE.rl("default_driver"); }

		@Override
		public void addProbeInfo(ProbeMode mode, IProbeInfo probeInfo, Player player, Level world,
				BlockState blockState, IProbeHitData data) {
			Block block = blockState.getBlock();
			if (block instanceof ITOPInfoProvider provider) {
				ITOPDriver driver = provider.getTheOneProbeDriver();
				if (driver != null) {
					driver.addProbeInfo(mode, probeInfo, player, world, blockState, data);
				}
			}

		}
	}

}
