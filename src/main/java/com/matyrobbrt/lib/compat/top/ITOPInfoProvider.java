package com.matyrobbrt.lib.compat.top;

import net.minecraft.block.Block;

/**
 * Implement this class in a {@link Block} class. In order to not throw
 * {@link ClassNotFoundException}s, make the driver a separate class!
 * 
 * @author matyrobbrt
 *
 */
public interface ITOPInfoProvider {

	ITOPDriver getTheOneProbeDriver();

}
