package com.gem.entity;

import com.gem.component.ComponentBase;
import com.gem.component.ComponentCamera;
import com.gem.engine.TypeObserver;

import lombok.val;

public class CameraObserver implements TypeObserver{
	private final EntityFactory entityFactory;
	
	
	public CameraObserver(EntityFactory entityFactory) {
		this.entityFactory = entityFactory;
	}

	@Override
	public void Added(ComponentBase obj) {
		// check if we added a camera component
	}

	@Override
	public void ChangedComponent(ComponentBase obj) {
		// we don't care if a camera is changed
	}

	@Override
	public void Parented(Entity ent, Entity parent) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void Removed(ComponentBase obj) {
		// TODO Auto-generated method stub		
	}

	@Override
	public void Reorder(Entity entity, Entity entity2) {
		// TODO Auto-generated method stub		
	}

}
