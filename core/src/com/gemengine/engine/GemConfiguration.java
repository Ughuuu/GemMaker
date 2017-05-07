package com.gemengine.engine;

import lombok.Builder;
import lombok.Getter;

@Builder
/**
 * Gem configuration class. Used to configure the game engine.
 * 
 * @author Dragos
 * 
 */
public class GemConfiguration {
	@Builder.Default
	@Getter
	/**
	 * Whether to use external files or only internal files. If this is false, no detection is used for the assets folder.
	 */
	private boolean useExternalFiles = false;
	@Builder.Default
	@Getter
	/**
	 * Whether the loading is done blocking or asynchronous.
	 */
	private boolean useBlockingLoad = false;
	@Builder.Default
	@Getter
	/**
	 * Whether to add the default loaders or not.
	 */
	private boolean useDefaultLoaders = false;
}
