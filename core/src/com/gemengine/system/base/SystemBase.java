package com.gemengine.system.base;

import lombok.Getter;
import lombok.Setter;

public abstract class SystemBase {
	@Getter
	@Setter
	private boolean enable;
	@Getter
	private final int priority;

	public SystemBase() {
		enable = true;
		priority = Integer.MAX_VALUE;
	}

	public SystemBase(boolean enable, int priority) {
		this.enable = enable;
		this.priority = priority;
	}

	public void onEnd() {
	}

	public void onInit() {
	}
}
