package com.gemengine.system.base;

import java.util.HashSet;
import java.util.Set;

import com.gemengine.entity.Entity;
import com.gemengine.system.ComponentSystem;

import lombok.Getter;

public abstract class ComponentUpdaterSystem extends SystemBase {
	@Getter
	private final Set<String> configuration;

	protected ComponentUpdaterSystem(ComponentSystem componentSystem) {
		this(componentSystem, new HashSet<String>(), true, Integer.MAX_VALUE);
	}

	protected ComponentUpdaterSystem(ComponentSystem componentSystem, Set<String> configuration, boolean enable,
			int priority) {
		super(enable, priority);
		this.configuration = configuration;
		componentSystem.addComponentUpdater(this);
	}

	public void onAfterEntities() {
	}

	public void onBeforeEntities() {
	}

	public void onNext(Entity ent) {
	}
}
