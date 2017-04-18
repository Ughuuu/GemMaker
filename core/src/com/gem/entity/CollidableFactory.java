package com.gem.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.gem.engine.EngineInfo;

/**
 * @author Dragos
 * @hidden
 */
public class CollidableFactory {
    public static Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
    public static World world;
    private static float density = 0.5f, friction = 0.4f, restituition = 0.6f;

    public static void createShapeBox(Body body, float w, float h) {
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(w, h);
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restituition;
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public static void createShapeCircle(Body body, float r) {
        CircleShape shape = new CircleShape();
        shape.setRadius(r);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restituition;
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public static void createShapePoly(Body body, Vector2[] verts) {
        PolygonShape shape = new PolygonShape();
        shape.set(verts);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = density;
        fixtureDef.friction = friction;
        fixtureDef.restitution = restituition;
        body.createFixture(fixtureDef);
        shape.dispose();
    }

    public static void setCoefficients(float dens, float fric, float rest) {
        density = dens;
        friction = fric;
        restituition = rest;
    }

    public Body createBody(BodyType t, Vector2 pos) {
        pos.scl(EngineInfo.MeterPerPixel);
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = t;
        bodyDef.position.set(pos);
        Body body = world.createBody(bodyDef);
        return body;
    }
}
