package com.gemengine.engine;

import lombok.Builder;
import lombok.Getter;

@Builder
public class GemConfiguration {
	@Builder.Default
	@Getter
	private boolean useExternalFiles = false;
	@Builder.Default
	@Getter
	private boolean useBlockingLoad = false;
	@Builder.Default
	@Getter
	private boolean useDefaultLoaders = false;
}
