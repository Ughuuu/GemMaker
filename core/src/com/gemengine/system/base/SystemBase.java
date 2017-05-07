package com.gemengine.system.base;

import org.apache.logging.log4j.MarkerManager;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
/**
 * The base system for all other systems. Implement this if you want a system
 * with the basic events.
 * 
 * @author Dragos
 *
 */
public abstract class SystemBase implements Comparable<SystemBase> {
	/**
	 * Global last id , used to generate an unique id for each object.
	 */
	private static int lastId;
	@Getter
	@Setter
	/**
	 * If this is false, the system will not receive any events.
	 */
	private boolean enable;
	@Getter
	/**
	 * The systems are called on their events based on their priorities.
	 */
	private final int priority;
	@Getter
	/**
	 * This is used internally to represent systems.
	 */
	private final int id;

	/**
	 * Create a system with priority as Integer.MAX_VALUE(will be called last).
	 */
	protected SystemBase() {
		this(true, Integer.MAX_VALUE);
	}

	/**
	 * Create a system and configure the enable state and the priority.
	 * 
	 * @param enable
	 *            State of system
	 * @param priority
	 *            Priority of sytem.
	 */
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

	/**
	 * Called when the system is disposed of. Preceded by a call to
	 * {@link #onPause()}.
	 */
	public void onExit() {
	}

	/**
	 * Called when the system is started
	 */
	public void onInit() {
	}

	/**
	 * Called when the {@link Application} is paused, usually when it's not
	 * active or visible on screen. An Application is also paused before it is
	 * destroyed.
	 */
	public void onPause() {
	}

	/**
	 * Called when the {@link Application} is resumed from a paused state,
	 * usually when it regains focus.
	 */
	public void onResume() {
	}

	/**
	 * Called when the {@link Application} is resized. This can happen at any
	 * point during a non-paused state but will never happen before a call to
	 * {@link #onInit()}.
	 * 
	 * @param width
	 *            the new width in pixels
	 * @param height
	 *            the new height in pixels
	 */
	public void onResize(int width, int height) {
	}
}
