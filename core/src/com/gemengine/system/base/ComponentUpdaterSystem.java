package com.gemengine.system.base;

import java.util.HashSet;
import java.util.Set;

import com.gemengine.entity.Entity;
import com.gemengine.system.ComponentSystem;

import lombok.Getter;

/**
 * A Component Updater System is called only in the
 * {@link com.gemengine.system.ComponentSystem} update event. The Component
 * System is an Timed System that is called every 16 ms.
 * 
 * The systems are called based on their priorities.
 * 
 * @author Dragos
 *
 */
public abstract class ComponentUpdaterSystem extends SystemBase {
	@Getter
	/**
	 * The configuration of this listener. This is used by the
	 * {@link com.gemengine.system.ComponentSystem} to know what components to
	 * give to this listener. To construct this configuration, use
	 * {@link com.gemengine.system.helper.ListenerHelper#createConfiguration}
	 * ex. ListenerHelper.createConfiguration(CameraComponent.class) to listen
	 * only to CameraComponent types or
	 * ListenerHelper.createConfiguration(Component.class) to listen to all
	 * Component types(base class for all components).
	 * 
	 * @return
	 */
	private final Set<String> configuration;

	/**
	 * The component system has to be given in this constructor. The
	 * configuration will be set for no class and will have the priority as
	 * Integer.MAX_VALUE.
	 * 
	 * @param componentSystem
	 *            ComponentSystem
	 */
	ComponentUpdaterSystem(ComponentSystem componentSystem) {
		this(componentSystem, new HashSet<String>(), true, Integer.MAX_VALUE);
	}

	/**
	 * Here is the complete constructor, that configures this system fully. To
	 * obtain a configuration, call
	 * {@link com.gemengine.system.helper.ListenerHelper#createConfiguration}
	 * with the needed component types ex.
	 * ListenerHelper.createConfiguration(CameraComponent.class) to listen only
	 * to CameraComponent types or
	 * ListenerHelper.createConfiguration(Component.class) to listen to all
	 * Component types(base class for all components).
	 * 
	 * @param componentSystem
	 *            Component System
	 * @param configuration
	 *            the configuration of this systme
	 * @param enable
	 *            if this is false, this system won't operate
	 * @param priority
	 *            the order on which this system is called.
	 */
	ComponentUpdaterSystem(ComponentSystem componentSystem, Set<String> configuration, boolean enable, int priority) {
		super(enable, priority);
		this.configuration = configuration;
		componentSystem.addComponentUpdater(this);
	}

	/**
	 * Event called after the {@link #onNext} event
	 */
	public void onAfterEntities() {
	}

	/**
	 * Event called before the {@link #onNext} event
	 */
	public void onBeforeEntities() {
	}

	/**
	 * Here you will receive all the entities that match the given
	 * configuration.
	 */
	public void onNext(Entity ent) {
	}
}
