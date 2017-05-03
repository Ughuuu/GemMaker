package com.gemengine.entity;

import java.util.List;
import java.util.Set;

import com.gemengine.component.Component;
import com.gemengine.system.ComponentSystem;
import com.gemengine.system.EntitySystem;

import lombok.Getter;

public class Entity {
	private static int lastId;
	@Getter
	private final int id;
	@Getter
	private final String name;
	private final ComponentSystem componentSystem;
	private final EntitySystem entitySystem;

	public Entity(String name, EntitySystem entitySystem, ComponentSystem componentSystem) {
		this.name = name;
		this.id = lastId++;
		this.componentSystem = componentSystem;
		this.entitySystem = entitySystem;
	}

	public void addChild(Entity child) {
		entitySystem.parent(this, child);
	}

	public <T extends Component> T addComponent(Class<T> type) {
		return componentSystem.create(this, type);
	}

	public void deparent() {
		entitySystem.deparent(this);
	}

	public <T extends Component> T findComponent(Class<T> type) {
		List<T> components = componentSystem.find(this, type);
		if (components == null || components.isEmpty()) {
			return null;
		}
		return components.get(0);
	}

	public <T extends Component> List<T> findComponents(Class<T> type) {
		return componentSystem.find(this, type);
	}

	public Set<Entity> getChildren() {
		return entitySystem.getChildren(this);
	}

	public Set<Entity> getDescendants() {
		return entitySystem.getDescendants(this);
	}

	public Entity getParent() {
		return entitySystem.getParent(this);
	}

	public Set<Entity> getPredessesors() {
		return entitySystem.getPredessesors(this);
	}

	public boolean isParent() {
		return entitySystem.isParent(this);
	}

	public void remove() {
		entitySystem.remove(this);
	}

	public void removeComponent(Class<? extends Component> type) {
		componentSystem.remove(this, type);
	}

	public void unparent() {
		entitySystem.unparent(this);
	}
}
