package com.ngeen.systems;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.IntervalEntityProcessingSystem;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.ngeen.components.PhysicsComponent;
import com.ngeen.components.TransformComponent;
import com.ngeen.holder.Constant;

@Wire
public class PhysicsSystem extends IntervalEntityProcessingSystem {
	public World world = new World(Constant.GRAVITY, true);
	public Box2DDebugRenderer debugRenderer;

	@SuppressWarnings("unchecked")
	public PhysicsSystem() {
		super(Aspect.all(TransformComponent.class, PhysicsComponent.class),
				Constant.FPS);
		if(!Constant.RELEASE)
		debugRenderer = new Box2DDebugRenderer();
	}

	@Override
	protected void begin() {
		if (Constant.DEBUG)
			debugRenderer.render(world, Constant.CAMERA.combined);
		world.step(Constant.MS, 6, 2);
	}

	@Override
	protected void process(Entity e) {
		// TODO Auto-generated method stub

	}

}