package com.gemengine.system;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.gemengine.entity.Entity;
import com.gemengine.system.base.EntityListener;
import com.gemengine.system.base.EntityListener.EntityChangeType;
import com.gemengine.system.base.SystemBase;
import com.google.inject.Inject;

import lombok.val;

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

	public Entity create(String name) {
		if (entityNameToId.get(name) != null) {
			return null;
		}
		Entity ent = new Entity(name, this, componentSystem);
		entityNameToId.put(name, ent.getId());
		entities.put(ent.getId(), ent);
		for (val entityListener : entityListeners) {
			entityListener.onChange(EntityChangeType.ADD, ent, ent);
		}
		return ent;
	}

	public void deparent(Entity child) {
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
			entityListener.onChange(EntityChangeType.DEPARENTED, parent, child);
		}
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

	public Set<Entity> getChildren(Entity parent) {
		Set<Integer> childrenIds = entityToChildren.get(parent.getId());
		Set<Entity> children = new HashSet<Entity>();
		if (childrenIds == null) {
			return children;
		}
		for (int child : childrenIds) {
			children.add(find(child));
		}
		return children;
	}

	public Set<Entity> getDescendants(Entity parent) {
		Set<Integer> childrenIds = entityToChildren.get(parent.getId());
		Set<Entity> children = new HashSet<Entity>();
		if (childrenIds == null) {
			return children;
		}
		for (int child : childrenIds) {
			Entity childEntity = find(child);
			children.addAll(getDescendants(childEntity));
		}
		return children;
	}

	public Entity getParent(Entity child) {
		Integer parentId = entityToParent.get(child.getId());
		if (parentId == null) {
			return null;
		}
		return find(parentId);
	}

	public Set<Entity> getPredessesors(Entity child) {
		Integer parentId = entityToParent.get(child.getId());
		Set<Entity> parents = new HashSet<Entity>();
		if (parentId == null) {
			return parents;
		}
		Entity parent = find(parentId);
		parents.add(parent);
		parents.addAll(getPredessesors(parent));
		return parents;
	}

	public boolean isParent(Entity parent) {
		return entityToParent.get(parent.getId()) != null;
	}

	public void parent(Entity parent, Entity child) {
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
			entityListener.onChange(EntityChangeType.PARENTED, parent, child);
		}
	}

	public void remove(Entity ent) {
		remove(ent.getId());
	}

	public void remove(int id) {
		Entity ent = entities.remove(id);
		if (ent == null) {
			return;
		}
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
		Set<Integer> children = entityToChildren.get(parent.getId());
		if (children == null) {
			return;
		}
		for (int child : children) {
			deparent(find(child));
		}
	}
}