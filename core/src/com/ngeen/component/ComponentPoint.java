package com.ngeen.component;

import java.util.List;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentPoint extends ComponentBase {
	Matrix4 comb = new Matrix4();
	private final Vector3 Position, Scale, Rotation;
	public boolean Update = false;

	public ComponentPoint(Ngeen ng, Entity ent) {
		super(ng, ent);
		Position = new Vector3();
		Scale = new Vector3(1, 1, 1);
		Rotation = new Vector3();
		Recalculate();
	}

	public final Matrix4 getMatrix() {
		return comb;
	}

	public Vector3 getPosition() {
		return Position;
	}

	public final Vector3 getRotation() {
		return Rotation;
	}

	public final Vector3 getScale() {
		return Scale;
	}

	public final float getScaleX() {
		return Scale.x;
	}

	@Override
	protected void Load(Element element) throws Exception {
		Position.x = element.getChildByName("Position.x").getFloat("Float");
		Position.y = element.getChildByName("Position.y").getFloat("Float");
		Position.z = element.getChildByName("Position.z").getFloat("Float");
		Scale.x = element.getChildByName("Scale.x").getFloat("Float");
		Scale.y = element.getChildByName("Scale.y").getFloat("Float");
		Scale.z = element.getChildByName("Scale.z").getFloat("Float");
		Rotation.x = element.getChildByName("Rotation.x").getFloat("Float");
		Rotation.y = element.getChildByName("Rotation.y").getFloat("Float");
		Rotation.z = element.getChildByName("Rotation.z").getFloat("Float");
		Recalculate();
	}

	private void Recalculate() {
		Quaternion q = new Quaternion().setEulerAngles(Rotation.x, Rotation.y, Rotation.z);
		comb.set(Position.x, Position.y, Position.z, q.x, q.y, q.z, q.w, Scale.x, Scale.y, Scale.z);
		Update = true;
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component").attribute("_Type", _Type.getName()).element("Position.x")
				.attribute("Float", Position.x).pop().element("Position.y").attribute("Float", Position.y).pop()
				.element("Position.z").attribute("Float", Position.z).pop().element("Scale.x")
				.attribute("Float", Scale.x).pop().element("Scale.y").attribute("Float", Scale.y).pop()
				.element("Scale.z").attribute("Float", Scale.z).pop().element("Rotation.x")
				.attribute("Float", Rotation.x).pop().element("Rotation.y").attribute("Float", Rotation.y).pop()
				.element("Rotation.z").attribute("Float", Rotation.z).pop().pop();
	}

	public ComponentPoint setPosition(Vector3 position) {
		updateChildren(new Vector3(position).sub(Position), new Vector3(), new Vector3());
		Position.set(position);
		Recalculate();
		return this;
	}

	public ComponentPoint setRotation(Vector3 rotation) {
		updateChildren(new Vector3(), new Vector3(rotation).sub(Rotation), new Vector3());
		Rotation.set(rotation);
		Recalculate();
		return this;
	}

	public ComponentPoint setScale(float sc) {
		Scale.x = sc;
		Scale.y = sc;
		Scale.z = sc;
		Recalculate();
		return this;
	}

	public ComponentPoint setScale(Vector3 scale) {
		updateChildren(new Vector3(), new Vector3(), new Vector3(scale).sub(Scale));
		Scale.set(scale);
		Recalculate();
		return this;
	}

	protected void updateChildren(Vector3 pos, Vector3 rot, Vector3 sc) {
		List<Entity> entities = getOwner().getChildren();
		for (Entity ent : entities) {
			ComponentPoint point = ent.getComponent(ComponentPoint.class);
			point.setPosition(new Vector3(point.getPosition()).add(pos));
			point.setRotation(new Vector3(point.getRotation()).add(rot));
			point.setScale(new Vector3(point.getScale()).add(sc));
		}
	}
}
