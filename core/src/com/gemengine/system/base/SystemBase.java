package com.gemengine.system.base;

import lombok.Getter;
import lombok.Setter;

public abstract class SystemBase implements Comparable<SystemBase> {
	private static int lastId;
	@Getter
	@Setter
	private boolean enable;
	@Getter
	private final int priority;
	@Getter
	private final int id;

	protected SystemBase() {
		this(true, Integer.MAX_VALUE);
	}

	protected SystemBase(boolean enable, int priority) {
		this.enable = enable;
		this.priority = priority;
		id = lastId++;
	}

	public void onEnd() {
	}

	public void onInit() {
	}

	@Override
	public int compareTo(SystemBase other) {
		if (this.getPriority() < other.getPriority()) {
			return -1;
		}
		if (this.getPriority() > other.getPriority()) {
			return 1;
		}
		return 0;
	}
}
