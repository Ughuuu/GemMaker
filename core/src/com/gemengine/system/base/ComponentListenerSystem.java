package com.gemengine.system.base;

import com.gemengine.component.Component;
import com.gemengine.system.ComponentSystem;

public abstract class ComponentListenerSystem extends SystemBase implements ComponentListener {
	protected ComponentListenerSystem(ComponentSystem componentSystem) {
		this(componentSystem, true, Integer.MAX_VALUE);
	}

	protected ComponentListenerSystem(ComponentSystem componentSystem, boolean enable, int priority) {
		super(enable, priority);
		componentSystem.addComponentListener(this);
	}

	@Override
	public <T extends Component> void onChange(ComponentChangeType changeType, T component) {
	}

	@Override
	public <T extends Component> void onTypeChange(Class<T> type) {
	}
}
