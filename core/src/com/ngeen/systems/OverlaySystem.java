package com.ngeen.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.World;
import com.ngeen.components.CameraComponent;
import com.ngeen.components.TagComponent;
import com.ngeen.components.TransformComponent;
import com.ngeen.helper.ComputeHelper;
import com.ngeen.holder.Constant;

@Wire
public class OverlaySystem extends EntityProcessingSystem {
	private ShapeRenderer shapeRenderer;
	ComponentMapper<TransformComponent> transformMapper;
	ComponentMapper<TagComponent> tagComponent;
	public int x1, y1, x2, y2;

	@SuppressWarnings("unchecked")
	public OverlaySystem() {
		super(Aspect.all(TransformComponent.class));
		shapeRenderer = new ShapeRenderer();
	}

	private void changeCamera() {
		Matrix4 comb = Constant.CAMERA.getComponent(CameraComponent.class).camera.combined;
		Matrix4 pos = ComputeHelper.getCombined(Constant.CAMERA.getComponent(TransformComponent.class));
		Matrix4 cpy = comb.cpy();
		shapeRenderer.setProjectionMatrix(cpy.mul(pos));
	}

	private void drawSelection() {
		shapeRenderer.rect(x1, y1, x2 - x1, y2 - y1);
	}

	@Override
	public void begin() {
		changeCamera();
		shapeRenderer.begin(ShapeType.Line);
		drawSelection();
	}

	@Override
	protected void process(Entity e) {
		TransformComponent transform = transformMapper.get(e);
		shapeRenderer.rect(transform.position.x - 5, transform.position.y - 5, 5, 5);
	}

	@Override
	public void end() {
		shapeRenderer.end();
	}
}
