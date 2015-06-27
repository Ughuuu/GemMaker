package com.ngeen.systems;


import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.ngeen.components.TextComponent;
import com.ngeen.components.TextureComponent;
import com.ngeen.components.TransformComponent;
import com.ngeen.holder.Constant;
import com.ngeen.ui.Interface;

@Wire
public class RenderSystem extends EntityProcessingSystem {
	private SpriteBatch batch;

	@SuppressWarnings("unchecked")
	public RenderSystem() {
		super(Aspect.all(TransformComponent.class)
				.one(TextureComponent.class, TextComponent.class));
		this.batch = new SpriteBatch();
		Constant.BATCH = this.batch;
	}
	
	private void changeCamera(){
		batch.setProjectionMatrix(Constant.CAMERA.combined);
	}

	@Override
	protected void begin() {
		changeCamera();
		batch.begin();
	}
	
	@Override
	protected void process(Entity e) {	
	}
	
	@Override
	protected void end(){
		batch.end();
	}
	
}