package com.matyrobbrt.lib.wrench;

import net.minecraft.block.Block;

/**
 * Implement this class in a {@link Block}, so that you dont need to send IMC
 * data in order to have a behaviour for your block
 * 
 * @author matyrobbrt
 *
 */
public interface IWrenchUsable {

	IWrenchBehaviour getBehaviour();

}
