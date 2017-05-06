package com.gemengine.system.base;

import com.gemengine.entity.Entity;
import com.gemengine.listener.EntityListener;
import com.gemengine.system.EntitySystem;

public abstract class EntityListenerSystem extends SystemBase implements EntityListener {
	protected EntityListenerSystem(EntitySystem componentSystem) {
		this(componentSystem, true, Integer.MAX_VALUE);
	}

	protected EntityListenerSystem(EntitySystem entitySystem, boolean enable, int priority) {
		super(enable, priority);
		entitySystem.addEntityListener(this);
	}

	@Override
	public void onChange(EntityChangeType changeType, Entity first, Entity second) {
	}
}
