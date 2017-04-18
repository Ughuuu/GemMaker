package com.gem.systems;

import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.gem.engine.EngineInfo;
import com.gem.engine.Gem;
import com.gem.entity.Entity;

/**
 * @author Dragos
 * @hidden
 */
public class SystemPhysics extends SystemBase {
    public Box2DDebugRenderer debugRenderer;
    public World world = new World(EngineInfo.Gravity, true);

    public SystemPhysics(Gem ng, SystemConfiguration conf) {
        super(ng, conf);
        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void onBeforeUpdate() {
        world.step(EngineInfo.Ms, 6, 2);
    }

    @Override
    public void onUpdate(Entity ent) {
    }

}