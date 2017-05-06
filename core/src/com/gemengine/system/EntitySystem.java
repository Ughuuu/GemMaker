package com.gemengine.system;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.MarkerManager;

import com.gemengine.entity.Entity;
import com.gemengine.system.base.EntityListener;
import com.gemengine.system.base.EntityListener.EntityChangeType;
import com.gemengine.system.base.SystemBase;
import com.google.inject.Inject;

import lombok.val;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class EntitySystem extends SystemBase {
	private final Map<Integer, Entity> entities = new HashMap<Integer, Entity>();
	private final Map<String, Integer> entityNameToId = new HashMap<String, Integer>();
	private final Map<Integer, Integer> entityToParent = new HashMap<Integer, Integer>();
	private final Map<Integer, Set<Integer>> entityToChildren = new HashMap<Integer, Set<Integer>>();
	private final Set<EntityListener> entityListeners = new HashSet<EntityListener>();
	private final ComponentSystem componentSystem;

	@Inject
	public EntitySystem(ComponentSystem componentSystem) {
		super(true, 2);
		this.componentSystem = componentSystem;
	}

	public void addEntityListener(EntityListener entityListener) {
		entityListeners.add(entityListener);
	}

	public Set<Entity> children(Entity parent) {
		Set<Integer> childrenIds = entityToChildren.get(parent.id());
		Set<Entity> children = new HashSet<Entity>();
		if (childrenIds == null) {
			return children;
		}
		for (int child : childrenIds) {
			children.add(find(child));
		}
		return children;
	}

	public Entity create(String name) {
		if (entityNameToId.get(name) != null) {
			return null;
		}
		Entity ent = new Entity(name, this, componentSystem);
		entityNameToId.put(name, ent.id());
		entities.put(ent.id(), ent);
		for (val entityListener : entityListeners) {
			try {
				entityListener.onChange(EntityChangeType.ADD, ent, ent);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		return ent;
	}

	public void deparent(Entity child) {
		if (child == null) {
			return;
		}
		Integer parentId = entityToParent.remove(child.id());
		if (parentId == null) {
			return;
		}
		Entity parent = entities.get(parentId);
		if (parent == null) {
			return;
		}
		Set<Integer> children = entityToChildren.get(parentId);
		if (children != null) {
			children.remove(child.id());
		}
		for (val entityListener : entityListeners) {
			entityListener.onChange(EntityChangeType.DEPARENTED, parent, child);
		}
	}

	public Set<Entity> descendants(Entity parent) {
		Set<Integer> childrenIds = entityToChildren.get(parent.id());
		Set<Entity> children = new HashSet<Entity>();
		if (childrenIds == null) {
			return children;
		}
		for (int child : childrenIds) {
			Entity childEntity = find(child);
			children.addAll(descendants(childEntity));
		}
		return children;
	}

	public Entity find(int id) {
		Entity ent = entities.get(id);
		return ent;
	}

	public Entity find(String name) {
		Integer id = entityNameToId.get(name);
		if (id == null) {
			return null;
		}
		return find(id);
	}

	public boolean hasParent(Entity child) {
		return entityToChildren.get(child.id()) != null;
	}

	public boolean isParent(Entity parent) {
		return entityToParent.get(parent.id()) != null;
	}

	public Entity parent(Entity child) {
		Integer parentId = entityToParent.get(child.id());
		if (parentId == null) {
			return null;
		}
		return find(parentId);
	}

	public void parent(Entity parent, Entity child) {
		int parentId = parent.id();
		int childId = child.id();
		entityToParent.put(childId, parentId);
		Set<Integer> children = entityToChildren.get(parentId);
		if (children == null) {
			children = new HashSet<Integer>();
			entityToChildren.put(parentId, children);
		}
		children.add(childId);
		for (val entityListener : entityListeners) {
			entityListener.onChange(EntityChangeType.PARENTED, parent, child);
		}
	}

	public Set<Entity> predessesors(Entity child) {
		Integer parentId = entityToParent.get(child.id());
		Set<Entity> parents = new HashSet<Entity>();
		if (parentId == null) {
			return parents;
		}
		Entity parent = find(parentId);
		parents.add(parent);
		parents.addAll(predessesors(parent));
		return parents;
	}

	public void remove(Entity ent) {
		remove(ent.id());
	}

	public void remove(int id) {
		Entity ent = entities.remove(id);
		if (ent == null) {
			return;
		}
		log.debug(MarkerManager.getMarker("gem"), "Entity deleted: id {} name {}", ent.id(), ent.getName());
		unparent(ent);
		entityNameToId.remove(ent.getName());
		for (val entityListener : entityListeners) {
			entityListener.onChange(EntityChangeType.DELETE, ent, ent);
		}
	}

	public void remove(String name) {
		Integer id = entityNameToId.remove(name);
		if (id == null) {
			return;
		}
		Entity ent = entities.remove(id);
		unparent(ent);
		for (val entityListener : entityListeners) {
			entityListener.onChange(EntityChangeType.DELETE, ent, ent);
		}
	}

	public void removeChild() {

	}

	public void unparent(Entity parent) {
		Set<Integer> children = entityToChildren.get(parent.id());
		if (children == null) {
			return;
		}
		for (int child : children) {
			deparent(find(child));
		}
	}
}