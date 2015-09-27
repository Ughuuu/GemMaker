package com.ngeen.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.ngeen.components.CameraComponent;
import com.ngeen.components.TagComponent;
import com.ngeen.components.TextComponent;
import com.ngeen.components.TextureComponent;
import com.ngeen.components.TransformComponent;
import com.ngeen.holder.Constant;
import com.ngeen.ui.Interface;

@Wire
public class RenderSystem extends EntityProcessingSystem {
	private SpriteBatch batch;
	private ComponentMapper<TransformComponent> transformMapper;
	private ComponentMapper<TextureComponent> textureMapper;

	@SuppressWarnings("unchecked")
	public RenderSystem() {
		super(Aspect.all(TransformComponent.class, TextureComponent.class));
		this.batch = new SpriteBatch();
		Constant.BATCH = this.batch;
	}

	private void changeCamera() {
		//batch.setProjectionMatrix(Constant.CAMERA.combined);
	}

	@Override
	protected void begin() {
		changeCamera();
		batch.begin();
	}

	@Override
	protected void process(Entity e) {
		TransformComponent transform = transformMapper.get(e);
		TextureComponent texture = textureMapper.get(e);

		if (texture.draw == true && texture.tex != null) {
			texture.tex.setRotation(transform.angle);
			texture.tex.setScale(transform.scale.x, transform.scale.y);
			texture.tex.setPosition(transform.position.x, transform.position.y);
			texture.tex.draw(batch);
		}
	}

	@Override
	protected void end() {
		batch.end();
	}

}