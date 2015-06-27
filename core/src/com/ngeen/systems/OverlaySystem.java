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
import com.ngeen.components.TagComponent;
import com.ngeen.components.TransformComponent;
import com.ngeen.holder.Constant;

@Wire
public class OverlaySystem extends EntityProcessingSystem {
	private ShapeRenderer shapeRenderer;
	ComponentMapper<TransformComponent> transformMapper;
	ComponentMapper<TagComponent> tagComponent;

	@SuppressWarnings("unchecked")
	public OverlaySystem() {
		super(Aspect.all(TransformComponent.class));
		shapeRenderer = new ShapeRenderer();
	}

	private void changeCamera() {
		shapeRenderer.setProjectionMatrix(Constant.CAMERA.combined);
	}

	@Override
	public void begin() {
		shapeRenderer.begin(ShapeType.Line);
		changeCamera();
	}

	@Override
	protected void process(Entity e) {
		TransformComponent transform = transformMapper.get(e);
		shapeRenderer.rect(transform.position.x - 5, transform.position.y - 5,
				transform.position.x + 5, transform.position.y + 5);
	}

	@Override
	public void end() {
		shapeRenderer.end();
	}
}
