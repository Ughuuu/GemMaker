package com.ngeen.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.physics.box2d.World;
import com.ngeen.component.TagComponent;
import com.ngeen.component.TransformComponent;
import com.ngeen.engine.Constant;

@Wire
public class TransformSystem extends EntityProcessingSystem {
	ComponentMapper<TransformComponent> transformMapper;
	ComponentMapper<TagComponent> tagComponent;

	@SuppressWarnings("unchecked")
	public TransformSystem() {
		super(Aspect.all(TransformComponent.class));
	}

	@Override
	public void begin() {
	}

	@Override
	protected void process(Entity e) {
		TransformComponent transform = transformMapper.get(e);
	}

	@Override
	public void end() {
	}
}
