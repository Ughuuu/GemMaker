package com.ngeen.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.ngeen.components.AnimationComponent;
import com.ngeen.components.TextureComponent;
import com.ngeen.components.TransformComponent;

@Wire
public class AnimateSystem extends EntityProcessingSystem{

	@SuppressWarnings("unchecked")
	public AnimateSystem() {
		super(Aspect.all(TransformComponent.class, AnimationComponent.class, TextureComponent.class));
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void process(Entity e) {
		// TODO Auto-generated method stub
		
	}

}
