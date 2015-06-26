package com.ngeen.systems;

import com.artemis.Aspect;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.ngeen.components.ButtonComponent;
import com.ngeen.components.TransformComponent;

@Wire
public class ButtonSystem extends EntityProcessingSystem {
	public boolean clickOnce = true;

	@SuppressWarnings("unchecked")
	public ButtonSystem() {
		super(Aspect.all(TransformComponent.class, ButtonComponent.class));
	}

	@Override
	protected void process(com.artemis.Entity e) {
		// TODO Auto-generated method stub
		
	}

}
