package com.gemengine.entity;

import java.util.List;

import com.gemengine.component.Component;
import com.gemengine.system.ComponentSystem;
import com.google.inject.Inject;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class Entity {
	@Getter
	private final int id;
	@Getter
	private final String name;
	private static int lastId;
	private final ComponentSystem componentSystem;

	public Entity(String name, ComponentSystem componentSystem) {
		this.name = name;
		this.id = lastId++;
		this.componentSystem = componentSystem;
	}

	public <T extends Component> T addComponent(Class<T> type) {
		return componentSystem.create(this, type);
	}

	public void removeComponent(Class<? extends Component> type) {
		componentSystem.remove(this, type);
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
}
