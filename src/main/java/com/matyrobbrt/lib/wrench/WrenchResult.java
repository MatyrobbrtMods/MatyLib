package com.matyrobbrt.lib.wrench;

/**
 * The result of a block being hit by a {@link WrenchItem}. {@link #CONSUME}
 * will cancel any other behaviour, and {@link #FAIL} will let the next
 * behaviour take action.
 * 
 * @author matyrobbrt
 *
 */
public enum WrenchResult {

	/**
	 * Will cancel any other {@link IWrenchBehaviour} from being executed.
	 */
	CONSUME,

	/**
	 * Will let the next {@link IWrenchBehaviour} take action;
	 */
	FAIL;
}
