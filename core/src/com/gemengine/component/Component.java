package com.gemengine.component;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

@Log4j2
/**
 * The basic Component type. This needs to be extended by components that will
 * be used in the game. Components hold data only, and the logic is performed by
 * {@link com.gemengine.system.base.SystemBase} or others that extend that. If
 * you need to do logic in the component, {@link com.google.inject.Inject} the
 * component with a system.
 * 
 * @author Dragos
 * 
 */
public abstract class Component {
	/**
	 * Global last id , used to generate an unique id for each object.
	 */
	private static int lastId;
	@Getter
	@Setter
	/**
	 * The component activity state. If this is false, the component will not be
	 * active for most systems and listeners.
	 */
	private boolean enable = true;
	@Getter
	/**
	 * This is used internally to represent components.
	 */
	private final int id;

	/**
	 * Constructs a component and generates an unique id for it.
	 */
	public Component() {
		id = lastId++;
	}

	/**
	 * Event to be used for initialization part, after the systems know of the
	 * component construction.
	 */
	public void onCreate() {
	}
}
