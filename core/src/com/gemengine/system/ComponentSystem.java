package com.gemengine.system;

import com.gemengine.system.base.TimedSystem;

public class ComponentSystem extends TimedSystem {
	public static final String componentSourceFolder = "component/";

	public ComponentSystem() {
		super(100, true, 1);
	}

	@Override
	public void onUpdate(float delta) {
	}
}