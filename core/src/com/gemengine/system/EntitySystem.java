package com.gemengine.system;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.gemengine.entity.Entity;
import com.gemengine.system.base.SystemBase;
import com.google.inject.Inject;

import com.gemengine.system.ComponentSystem;

public class EntitySystem extends SystemBase {
	private final Map<Integer, Entity> entities = new HashMap<Integer, Entity>();
	private final Map<String, Integer> entityNameToId = new HashMap<String, Integer>();
	private final Map<Integer, Integer> entityToParent = new HashMap<Integer, Integer>();
	private final Map<Integer, List<Integer>> entityToChildren = new HashMap<Integer, List<Integer>>();
	private final ComponentSystem componentSystem;

	@Inject
	public EntitySystem(ComponentSystem componentSystem) {
		super(true, 2);
		this.componentSystem = componentSystem;
	}

	public Entity create(String name) {
		if (entityNameToId.get(name) != null) {
			return null;
		}
		Entity ent = new Entity(name, componentSystem);
		entityNameToId.put(name, ent.getId());
		entities.put(ent.getId(), ent);
		return ent;
	}

	public Entity find(int id) {
		return entities.get(id);
	}

	public Entity find(String name) {
		Integer id = entityNameToId.get(name);
		if (id == null) {
			return null;
		}
		return find(id);
	}

	public void remove(int id) {
		Entity ent = entities.remove(id);
		if (ent == null) {
			return;
		}
		entityNameToId.remove(ent.getName());
	}

	public void remove(String name) {
		Integer id = entityNameToId.remove(name);
		if (id == null) {
			return;
		}
		entities.remove(id);
	}
	
	public void remove(Entity ent){
		remove(ent.getId());
	}
	
	public void addChild(Entity parent, Entity child){
		entityToParent.put(child.getId(), parent.getId());
	}
	
	public void removeChild(){
		
	}
}