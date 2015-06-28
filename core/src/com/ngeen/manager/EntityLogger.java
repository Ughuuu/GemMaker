package com.ngeen.manager;

import com.artemis.Entity;
import com.artemis.Manager;
import com.ngeen.ui.Interface;

public class EntityLogger extends Manager{
	@Override
	public void added(Entity e) {
		Interface.addEntity(e);
	}

	@Override
	public void changed(Entity e) {
		Interface.changeEntity(e);
	}

	@Override
	public void deleted(Entity e) {
		Interface.deleteEntity(e);
	}
	
	@Override
	public void disabled(Entity e) {}

	@Override
	public void enabled(Entity e) {}
}
