package com.gemengine.listener;

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
public interface ComponentUpdaterListener extends PriorityListener {
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
	public Set<String> getConfiguration();

	/**
	 * Event called after the {@link #onNext} event
	 */
	public void onAfterEntities();

	/**
	 * Event called before the {@link #onNext} event
	 */
	public void onBeforeEntities();

	/**
	 * Here you will receive all the entities that match the given
	 * configuration.
	 */
	public void onNext(Entity ent);
}
