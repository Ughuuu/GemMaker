package com.ngeen.factories;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.ngeen.holder.Constant;

public class CollidableFactory {
	public World world;
	public Box2DDebugRenderer debugRenderer = new Box2DDebugRenderer();
	float density=0.5f, friction=0.4f, restituition=0.6f;
	
	public void setCoefficients(float density, float friction, float restituition){
		this.density = density;
		this.friction = friction;
		this.restituition = restituition;
	}

	public void createShapeCircle(Body body, float r) {
		CircleShape shape = new CircleShape();
		shape.setRadius(r);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = density;
		fixtureDef.friction = friction;
		fixtureDef.restitution = restituition;
		Fixture fixture = body.createFixture(fixtureDef);
		shape.dispose();
	}

	public void createShapeBox(Body body, float w, float h) {
		PolygonShape shape = new PolygonShape();
		shape.setAsBox(w, h);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = density;
		fixtureDef.friction = friction;
		fixtureDef.restitution = restituition;
		Fixture fixture = body.createFixture(fixtureDef);
		shape.dispose();
	}

	public void createShapePoly(Body body, Vector2[] verts) {
		PolygonShape shape = new PolygonShape();
		shape.set(verts);

		FixtureDef fixtureDef = new FixtureDef();
		fixtureDef.shape = shape;
		fixtureDef.density = density;
		fixtureDef.friction = friction;
		fixtureDef.restitution = restituition;
		Fixture fixture = body.createFixture(fixtureDef);
		shape.dispose();
	}

	public Body createBody(BodyType t, Vector2 pos) {
		pos.scl(Constant.INV_PIXEL_TO_METER);
		BodyDef bodyDef = new BodyDef();
		bodyDef.type = t;
		bodyDef.position.set(pos);
		Body body = world.createBody(bodyDef);
		return body;
	}
}
