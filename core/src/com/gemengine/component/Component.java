package com.gemengine.component;

import org.apache.logging.log4j.MarkerManager;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class Component {
	private static int lastId;
	@Getter
	@Setter
	private boolean enable = true;
	@Getter
	private final int id;

	public Component() {
		id = lastId++;
		log.debug(MarkerManager.getMarker("gem"), "Component created: id {} type {}", id, this.getClass());
	}

	public void onCreate() {
	}
}
