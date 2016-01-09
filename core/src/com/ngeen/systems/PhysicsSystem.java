package com.ngeen.systems;

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.ngeen.engine.Constant;
import com.ngeen.entity.Entity;

public class PhysicsSystem extends SystemBase {
	public World world = new World(Constant.GRAVITY, true);
	public Box2DDebugRenderer debugRenderer;

	public PhysicsSystem() {
		super(new SystemConfiguration().all());
		debugRenderer = new Box2DDebugRenderer();
	}

	public void onBeforeUpdate() {
		world.step(Constant.MS, 6, 2);
	};

	@Override
	public void onUpdate(Entity ent) {
	}

}