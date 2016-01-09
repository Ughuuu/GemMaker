package com.ngeen.systems;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Matrix4;
import com.ngeen.component.ComponentDrawble;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class OverlaySystem extends SystemBase {
	private ShapeRenderer shapeRenderer;
	public static float x1, y1, x2, y2;

	@SuppressWarnings("unchecked")
	public OverlaySystem() {
		super(new SystemConfiguration().all(ComponentDrawble.class));
		shapeRenderer = new ShapeRenderer();
	}

	private void drawSelection() {
		Matrix4 comb = null;// =
							// Constant.UI_CAMERA.getComponent(CameraComponent.class).camera.combined;
		shapeRenderer.setProjectionMatrix(comb);
		shapeRenderer.rect(x1, y1, x2 - x1, y2 - y1);
	}

	@Override
	public void onBeforeUpdate() {
		shapeRenderer.begin(ShapeType.Line);
		drawSelection();
	}

	@Override
	public void onUpdate(Entity ent) {
		/*
		 * TransformComponent transform = transformMapper.get(e);
		 * MaterialComponent texture = textureMapper.get(e); float w = 1, h = 1;
		 * if (texture != null) { w = texture.tex.getWidth(); h =
		 * texture.tex.getHeight(); }
		 * 
		 * Matrix4 comb =
		 * Constant.CAMERA.getComponent(CameraComponent.class).camera.combined;
		 * Matrix4 pos = ComputeHelper.getCombined(Constant.CAMERA.getComponent(
		 * TransformComponent.class)); Matrix4 pos2 =
		 * ComputeHelper.getCombined(transform);
		 * shapeRenderer.setProjectionMatrix(pos2.mul(pos).mul(comb));
		 * 
		 * shapeRenderer.rect(-w / 2, -h / 2, w, h);
		 */
	}

	@Override
	public void onAfterUpdate() {
		shapeRenderer.end();
	}
}
