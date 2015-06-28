package com.ngeen.systems;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.managers.TagManager;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.physics.box2d.World;
import com.ngeen.components.TagComponent;
import com.ngeen.components.TransformComponent;
import com.ngeen.holder.Constant;

@Wire
public class LogSystem extends EntityProcessingSystem {
	private ComponentMapper<TransformComponent> transformMapper;
	private ComponentMapper<TagComponent> tagMapper;

	@SuppressWarnings("unchecked")
	public LogSystem() {
		super(Aspect.all(TransformComponent.class));
	}

	@Override
	public void begin() {
		Constant.BATCH.begin();
		Constant.DEBUG_FONT.getData().setScale(0.15f);
	}

	@Override
	protected void process(Entity e) {
		TransformComponent transform = transformMapper.get(e);
		String text = tagMapper.get(e).name;
		Constant.DEBUG_FONT.draw(Constant.BATCH, text, transform.position.x,
				transform.position.y, 0, 1, false);
	}

	@Override
	public void end() {
		Constant.BATCH.end();
	}
}
