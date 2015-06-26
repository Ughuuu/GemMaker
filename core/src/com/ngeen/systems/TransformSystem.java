package com.ngeen.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.physics.box2d.World;
import com.ngeen.components.TransformComponent;

@Wire
public class TransformSystem extends EntityProcessingSystem {

	@SuppressWarnings("unchecked")
	public TransformSystem() {
		super(Aspect.all(TransformComponent.class));
	}

	@Override
	protected void process(Entity e) {
		// TODO Auto-generated method stub
		
	}
}
