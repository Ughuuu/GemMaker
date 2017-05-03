package com.gemengine.system.base;

import java.util.HashSet;
import java.util.Set;

import com.gemengine.component.Component;
import com.gemengine.system.ComponentSystem;

import lombok.Getter;

public abstract class ComponentListenerSystem extends SystemBase implements ComponentListener {
	@Getter
	private final Set<String> configuration;

	protected ComponentListenerSystem(ComponentSystem componentSystem) {
		this(componentSystem, new HashSet<String>(), true, Integer.MAX_VALUE);
	}

	protected ComponentListenerSystem(ComponentSystem componentSystem, Set<String> configuration, boolean enable,
			int priority) {
		super(enable, priority);
		this.configuration = configuration;
		componentSystem.addComponentListener(this);
	}

	@Override
	public <T extends Component> void onChange(ComponentChangeType changeType, T component) {
	}

	@Override
	public <T extends Component> void onTypeChange(Class<T> type) {
	}
}
