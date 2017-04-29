package com.gemengine.system.base;

import lombok.Getter;

public abstract class TimedSystem extends SystemBase {
	/**
	 * The repeat interval in milliseconds. Default is 16, aka 60 fps.
	 */
	@Getter
	private final float interval;

	protected TimedSystem() {
		this.interval = 16;
	}

	protected TimedSystem(float interval, boolean enable, int priority) {
		super(enable, priority);
		this.interval = interval;
	}

	public void onUpdate(float delta) {
	}
}
