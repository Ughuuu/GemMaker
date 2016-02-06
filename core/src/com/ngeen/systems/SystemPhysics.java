package com.ngeen.systems;

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.ngeen.engine.EngineInfo;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

/**
 * @hidden
 * @author Dragos
 *
 */
public class SystemPhysics extends SystemBase {
	public World world = new World(EngineInfo.Gravity, true);
	public Box2DDebugRenderer debugRenderer;

	public SystemPhysics(Ngeen ng, SystemConfiguration conf) {
		super(ng, conf);
		debugRenderer = new Box2DDebugRenderer();
	}

	@Override
	public void onBeforeUpdate() {
		world.step(EngineInfo.Ms, 6, 2);
	};

	@Override
	public void onUpdate(Entity ent) {
	}

}