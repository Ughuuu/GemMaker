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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;
import com.ngeen.component.CameraComponent;
import com.ngeen.component.MaterialComponent;
import com.ngeen.component.TagComponent;
import com.ngeen.component.TransformComponent;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.Constant;
import com.ngeen.helper.ComputeHelper;

@Wire
public class OverlaySystem extends EntityProcessingSystem {
	private ShapeRenderer shapeRenderer;
	ComponentMapper<TransformComponent> transformMapper;
	ComponentMapper<MaterialComponent> textureMapper;
	ComponentMapper<TagComponent> tagComponent;
	public static float x1, y1, x2, y2;

	@SuppressWarnings("unchecked")
	public OverlaySystem() {
		super(Aspect.all(TransformComponent.class));
		shapeRenderer = new ShapeRenderer();
	}

	private void drawSelection() {
		Matrix4 comb = Constant.UI_CAMERA.getComponent(CameraComponent.class).camera.combined;
		shapeRenderer.setProjectionMatrix(comb);
		shapeRenderer.rect(x1, y1, x2 - x1, y2 - y1);
	}

	@Override
	public void begin() {
		shapeRenderer.begin(ShapeType.Line);
		drawSelection();
	}

	@Override
	protected void process(Entity e) {
		TransformComponent transform = transformMapper.get(e);
		MaterialComponent texture = textureMapper.get(e);
		float w = 1, h = 1;
		if (texture != null) {
			w = texture.tex.getWidth();
			h = texture.tex.getHeight();
		}

		Matrix4 comb = Constant.CAMERA.getComponent(CameraComponent.class).camera.combined;
		Matrix4 pos = ComputeHelper.getCombined(Constant.CAMERA.getComponent(TransformComponent.class));
		Matrix4 pos2 = ComputeHelper.getCombined(transform);
		shapeRenderer.setProjectionMatrix(pos2.mul(pos).mul(comb));

		shapeRenderer.rect(-w / 2, -h / 2, w, h);
	}

	@Override
	public void end() {
		shapeRenderer.end();
	}
}
