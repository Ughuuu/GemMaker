package com.gemengine.listener;

import com.gemengine.entity.Entity;

/**
 * Listener interface used for entities. Extend this and add yourself as a
 * listener in the {@link com.gemengine.system.EntitySystem} by calling
 * {@link com.gemengine.system.EntitySystem#addEntityListener}
 * 
 * @author Dragos
 *
 */
public interface EntityListener {
	/**
	 * The entity change event.
	 * 
	 * @author Dragos
	 *
	 */
	public static enum EntityChangeType {
		/**
		 * A new entity is created.
		 */
		ADD,
		/**
		 * An entity is deleted.
		 */
		DELETE,
		/**
		 * An entity is added as a child to another.
		 */
		PARENTED,
		/**
		 * An entity is removed as a child from another.
		 */
		DEPARENTED
	}

	/**
	 * The change event callback for entities. For add and delete event, first
	 * and second entity are the same. For parented and deparented, the first is
	 * the parent and the second is the child.
	 * 
	 * @param changeType
	 *            The change type
	 * @param first
	 *            The first entity(in case of parented or deparented, this is
	 *            the parent, otherwise first and second are the same).
	 * @param second
	 *            The second entity(in case of parented or deparented, this is
	 *            the child, otherwise first and second are the same).
	 */
	public void onChange(EntityChangeType changeType, Entity first, Entity second);
}
