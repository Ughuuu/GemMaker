package com.gemengine.listener;

import com.gemengine.component.Component;
import com.gemengine.entity.Entity;

/**
 * Listener interface used for components located on an entity. Extend this and
 * add yourself as a listener in the
 * {@link com.gemengine.system.ComponentSystem} by calling
 * {@link com.gemengine.system.ComponentSystem#addEntityComponentListener}
 * 
 * @author Dragos
 *
 */
public interface EntityComponentListener extends PriorityListener{
	/**
	 * Notifications triggered by components when something changed.
	 * 
	 * @param event
	 *            The event name
	 * @param notifier
	 *            The event triggerer.
	 */
	public <T extends Component> void onNotify(String event, T notifier);

	/**
	 * Get the entity that you listen events for.
	 * 
	 * @return An entity.
	 */
	public Entity getOwner();
}
