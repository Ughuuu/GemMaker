package com.gemengine.system.base;

import org.apache.logging.log4j.MarkerManager;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
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
		log.debug(MarkerManager.getMarker("gem"), "System created: id {} type {}", id, this.getClass());
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

	public void onEnd() {
	}

	public void onInit() {
	}
}
