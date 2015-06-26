package com.ngeen.systems;


import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.ngeen.components.TextComponent;
import com.ngeen.components.TextureComponent;
import com.ngeen.components.TransformComponent;
import com.ngeen.holder.Constant;

@Wire
public class RenderSystem extends EntityProcessingSystem {
	private SpriteBatch batch;

	@SuppressWarnings("unchecked")
	public RenderSystem(SpriteBatch batch) {
		super(Aspect.all(TransformComponent.class)
				.one(TextureComponent.class, TextComponent.class));
		this.batch = batch;
	}

	@Override
	protected void begin() {
		Gdx.gl.glClearColor(Constant.BACKGROUND_COLOR.r, Constant.BACKGROUND_COLOR.g, Constant.BACKGROUND_COLOR.b, Constant.BACKGROUND_COLOR.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();
	}
	
	@Override
	protected void process(Entity e) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	protected void end(){
		
	}
	
}