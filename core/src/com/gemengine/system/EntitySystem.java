package com.gemengine.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.MarkerManager;

import com.gemengine.component.Component;
import com.gemengine.entity.Entity;
import com.gemengine.listener.EntityListener;
import com.gemengine.listener.PriorityListener;
import com.gemengine.listener.EntityListener.EntityChangeType;
import com.gemengine.system.base.SystemBase;
import com.google.inject.Inject;

import lombok.val;
import lombok.extern.log4j.Log4j2;

/**
 * The entity system, used to create, destroy and query entities.
 * 
 * @author Dragos
 *
 */
@Log4j2
public class EntitySystem extends SystemBase {
	private final Map<Integer, Entity> entities = new HashMap<Integer, Entity>();
	private final Map<String, Integer> entityNameToId = new HashMap<String, Integer>();
	private final Map<Integer, Integer> entityToParent = new HashMap<Integer, Integer>();
	private final Map<Integer, Set<Integer>> entityToChildren = new HashMap<Integer, Set<Integer>>();
	private final List<EntityListener> entityListeners = new ArrayList<EntityListener>();
	private final ComponentSystem componentSystem;
	private final TimingSystem timingSystem;

	@Inject
	public EntitySystem(ComponentSystem componentSystem, TimingSystem timingSystem) {
		super(true, 2);
		this.componentSystem = componentSystem;
		this.timingSystem = timingSystem;
	}

	/**
	 * Add an {@link EntityListener}. This listens to all entity
	 * changes(add/remove).
	 * 
	 * @param entityListener
	 */
	public void addEntityListener(EntityListener entityListener) {
		entityListeners.add(entityListener);
		Collections.sort(entityListeners, PriorityListener.getComparator());
	}

	/**
	 * Create a new entity with the given name. It has to be unique.
	 * 
	 * @param name
	 *            The name of the entity
	 * @return A new entity or old entity
	 */
	public Entity create(String name) {
		Integer oldEntity = entityNameToId.get(name);
		if (oldEntity != null) {
			return get(oldEntity);
		}
		Entity ent = new Entity(name, this, componentSystem);
		entityNameToId.put(name, ent.getId());
		entities.put(ent.getId(), ent);
		for (val entityListener : entityListeners) {
			if (!entityListener.isEnable()) {
				continue;
			}
			try {
				entityListener.onChange(EntityChangeType.ADD, ent, ent);
			} catch (Throwable t) {
				log.warn(MarkerManager.getMarker("gem"), "Entity event", t);
			}
		}
		return ent;
	}

	/**
	 * Delete the given entity.
	 * 
	 * @param ent
	 *            the entity to delete
	 */
	public void delete(Entity ent) {
		delete(ent.getId());
	}

	/**
	 * Delete the given entity.
	 * 
	 * @param id
	 *            the id of the entity to delete
	 */
	public void delete(int id) {
		Entity ent = entities.get(id);
		if (!entityListeners.isEmpty()) {
			for (val entityListener : entityListeners) {
				if (!entityListener.isEnable()) {
					continue;
				}
				try {
					entityListener.onChange(EntityChangeType.DELETE, ent, ent);
				} catch (Throwable t) {
					log.warn(MarkerManager.getMarker("gem"), "Entity event", t);
				}
			}
		}
		componentSystem.clear(ent);
		entities.remove(id);
		if (ent == null) {
			return;
		}
		unlinkChildren(ent);
		entityNameToId.remove(ent.getName());
	}

	/**
	 * Delete the given entity.
	 * 
	 * @param name
	 *            the name of the entity to delete
	 */
	public void delete(String name) {
		Entity ent = get(name);
		if (!entityListeners.isEmpty()) {
			for (val entityListener : entityListeners) {
				if (!entityListener.isEnable()) {
					continue;
				}
				try {
					entityListener.onChange(EntityChangeType.DELETE, ent, ent);
				} catch (Throwable t) {
					log.warn(MarkerManager.getMarker("gem"), "Entity event", t);
				}
			}
		}
		componentSystem.clear(ent);
		Integer id = entityNameToId.remove(name);
		if (id == null) {
			return;
		}
		entities.remove(id);
		unlinkChildren(ent);
	}

	/**
	 * Unlink the child from its parent. Does not throw.
	 * 
	 * @param child
	 *            The child to be removed from its parent logical list.
	 */
	public void unlinkParent(Entity child) {
		if (child == null) {
			return;
		}
		Integer parentId = entityToParent.remove(child.getId());
		if (parentId == null) {
			return;
		}
		Entity parent = entities.get(parentId);
		if (parent == null) {
			return;
		}
		Set<Integer> children = entityToChildren.get(parentId);
		if (children != null) {
			children.remove(child.getId());
		}
		for (val entityListener : entityListeners) {
			if (!entityListener.isEnable()) {
				continue;
			}
			try {
				entityListener.onChange(EntityChangeType.DEPARENTED, parent, child);
			} catch (Throwable t) {
				log.warn(MarkerManager.getMarker("gem"), "Entity event", t);
			}
		}
	}

	/**
	 * Get the entity by its id.
	 * 
	 * @param id
	 *            The id of the entity.
	 * @return The entity or null
	 */
	public Entity get(int id) {
		return entities.get(id);
	}

	/**
	 * Get the entity by its name
	 * 
	 * @param name
	 *            The name of the entity
	 * @return The entity or null
	 */
	public Entity get(String name) {
		Integer id = entityNameToId.get(name);
		if (id == null) {
			return null;
		}
		return get(id);
	}

	/**
	 * Get the children of an entity
	 * 
	 * @param parent
	 *            The parent entity
	 * @return A non null value representing the children of this entity.
	 */
	public Set<Entity> getChildren(Entity parent) {
		Set<Integer> childrenIds = entityToChildren.get(parent.getId());
		Set<Entity> children = new HashSet<Entity>();
		if (childrenIds == null) {
			return children;
		}
		for (int child : childrenIds) {
			children.add(get(child));
		}
		return children;
	}

	/**
	 * Get the children of this entity and the children of those, recursively,
	 * until there are no more children found.
	 * 
	 * @param parent
	 *            the parent entity
	 * @return The descendants.
	 */
	public Set<Entity> getDescendants(Entity parent) {
		Set<Integer> childrenIds = entityToChildren.get(parent.getId());
		Set<Entity> children = new HashSet<Entity>();
		if (childrenIds == null) {
			return children;
		}
		for (int child : childrenIds) {
			Entity childEntity = get(child);
			children.add(childEntity);
			children.addAll(getDescendants(childEntity));
		}
		return children;
	}

	/**
	 * Get the children of this entity and the children of those, recursively,
	 * until there are no more children found.
	 * 
	 * @param parent
	 *            the parent entity
	 * @return The descendants.
	 */
	public <T extends Component> Set<Entity> getFirstDescendantsOf(Entity parent, Class<T> componentType) {
		Set<Integer> childrenIds = entityToChildren.get(parent.getId());
		Set<Entity> children = new HashSet<Entity>();
		if (childrenIds == null) {
			return children;
		}
		for (int child : childrenIds) {
			Entity childEntity = get(child);
			if (childEntity.getComponent(componentType) != null) {
				children.add(childEntity);
			} else {
				children.addAll(getDescendants(childEntity));
			}
		}
		return children;
	}

	/**
	 * Get the parent of this entity or null.
	 * 
	 * @param child
	 *            the child entity
	 * @return An entity or null.
	 */
	public Entity getParent(Entity child) {
		Integer parentId = entityToParent.get(child.getId());
		if (parentId == null) {
			return null;
		}
		return get(parentId);
	}

	/**
	 * Set the parent entity to be the parent of the child entity.
	 * 
	 * @param parent
	 *            The parent entity
	 * @param child
	 *            The child entity
	 */
	public void setParent(Entity parent, Entity child) {
		int parentId = parent.getId();
		int childId = child.getId();
		entityToParent.put(childId, parentId);
		Set<Integer> children = entityToChildren.get(parentId);
		if (children == null) {
			children = new HashSet<Integer>();
			entityToChildren.put(parentId, children);
		}
		children.add(childId);
		for (val entityListener : entityListeners) {
			if (!entityListener.isEnable()) {
				continue;
			}
			try {
				entityListener.onChange(EntityChangeType.PARENTED, parent, child);
			} catch (Throwable t) {
				log.warn(MarkerManager.getMarker("gem"), "Entity event", t);
			}
		}
	}

	/**
	 * Get the predecessors of this entity. This is a collection of this
	 * entities parent and the parent of that one, recursively until no more
	 * parents are found.
	 * 
	 * @param child
	 *            the child to get the predecessors from
	 * @return The predecessors.
	 */
	public Set<Entity> getPredecessors(Entity child) {
		Integer parentId = entityToParent.get(child.getId());
		Set<Entity> parents = new HashSet<Entity>();
		if (parentId == null) {
			return parents;
		}
		Entity parent = get(parentId);
		parents.add(parent);
		parents.addAll(getPredecessors(parent));
		return parents;
	}

	public <T extends Component> Entity getFirstPredecessorOf(Entity child, Class<T> componentType) {
		Integer parentId = entityToParent.get(child.getId());
		if (parentId == null) {
			return null;
		}
		Entity parent = get(parentId);
		if (parent.getComponent(componentType) != null) {
			return parent;
		} else {
			return getFirstPredecessorOf(parent, componentType);
		}
	}

	/**
	 * Whether this entity has a parent or not.
	 * 
	 * @param child
	 *            the child entity to check the parent of
	 * @return True if this entity is a child.
	 */
	public boolean hasParent(Entity child) {
		return entityToChildren.get(child.getId()) != null;
	}

	/**
	 * Whether this entity has children or not.
	 * 
	 * @param parent
	 *            the parent entity to check that has children
	 * @return True if this entity is a parent.
	 */
	public boolean isParent(Entity parent) {
		return entityToParent.get(parent.getId()) != null;
	}

	/**
	 * Remove all the logical links between this entity and its children.
	 * 
	 * @param parent
	 *            the parent entity to unlink the children from
	 */
	public void unlinkChildren(Entity parent) {
		Set<Integer> children = entityToChildren.get(parent.getId());
		if (children == null) {
			return;
		}
		for (int child : children) {
			unlinkParent(get(child));
		}
	}
}