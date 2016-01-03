package com.ngeen.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.ngeen.component.CameraComponent;
import com.ngeen.component.MaterialComponent;
import com.ngeen.component.TagComponent;
import com.ngeen.component.TextComponent;
import com.ngeen.component.TransformComponent;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.Constant;
import com.ngeen.helper.ComputeHelper;

@Wire
public class RenderSystem extends EntityProcessingSystem {
	private SpriteBatch batch;
	private ComponentMapper<TransformComponent> transformMapper;
	private ComponentMapper<MaterialComponent> textureMapper;

	@SuppressWarnings("unchecked")
	public RenderSystem() {
		super(Aspect.all(TransformComponent.class, MaterialComponent.class));
		this.batch = new SpriteBatch();
		Constant.BATCH = this.batch;
	}

	private void changeCamera() {
		Matrix4 comb = Constant.CAMERA.getComponent(CameraComponent.class).camera.combined;
		Matrix4 pos = ComputeHelper.getCombined(Constant.CAMERA.getComponent(TransformComponent.class));
		batch.setProjectionMatrix(pos.mul(comb));
	}

	@Override
	protected void begin() {
		changeCamera();
		batch.begin();
	}

	@Override
	protected void process(Entity e) {
		/*
		TransformComponent transform = transformMapper.get(e);
		TextureComponent texture = textureMapper.get(e);

		if (texture.draw == true && texture.tex != null) {
			Sprite tex = texture.tex;
			if (Math.abs(transform.scale.x - tex.getScaleX()) > Constant.EPSILON
					|| Math.abs(transform.scale.y - tex.getScaleY()) > Constant.EPSILON) {
				tex.setScale(transform.scale.x, transform.scale.y);
			}
			if (Math.abs(transform.angle - tex.getRotation()) > Constant.EPSILON) {
				tex.setRotation(transform.angle);
			}
			if (Math.abs(transform.position.x - texture.tex.getWidth() * 0.5f - tex.getX()) > Constant.EPSILON || Math
					.abs(transform.position.y - texture.tex.getHeight() * 0.5f - tex.getY()) < Constant.EPSILON) {
				tex.setPosition(transform.position.x - texture.tex.getWidth() * 0.5f,
						transform.position.y - texture.tex.getHeight() * 0.5f);
			}
			texture.tex.draw(batch);
		}
		*/
	}

	@Override
	protected void end() {
		batch.end();
	}

}