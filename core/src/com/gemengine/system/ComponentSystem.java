package com.gemengine.system;

import java.util.Collection;
import java.util.Map;

import org.jsync.sync.Sync;

import com.gemengine.component.Component;
import com.gemengine.system.base.TimedSystem;

public class ComponentSystem extends TimedSystem {
	public static final String componentSourceFolder = "component/";
	Map<Class<?>, Sync<Component>> components;

	public ComponentSystem() {
		super(100, true, 1);
	}

	public Component createComponent(Class<?> type){
		return components.get(type).newInstance();
	}

	public Collection<Class<?>> getComponentTypes() {
		return components.keySet();
	}

	@Override
	public void onUpdate(float delta) {
	}
}