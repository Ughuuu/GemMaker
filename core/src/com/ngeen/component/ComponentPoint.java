package com.ngeen.component;

import java.util.List;
import java.util.Set;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentPoint extends ComponentBase {
	private final Vector3 Position, Scale, Rotation;
	Matrix4 comb = new Matrix4();
	public boolean Update = false;

	public ComponentPoint(Ngeen ng, Entity ent) {
		super(ng, ent);
		Position = new Vector3();
		Scale = new Vector3(1, 1, 1);
		Rotation = new Vector3();
		Recalculate();
	}

	private void Recalculate() {
		comb = comb.idt().translate(Position).scl(Scale)
				.rotate(new Quaternion().setEulerAngles(Rotation.x, Rotation.y, Rotation.z));
		Update = true;
	}

	public Vector3 getPosition() {
		return Position;
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

	public ComponentPoint setPosition(Vector3 position) {
		updateChildren(new Vector3(position).sub(Position), new Vector3(), new Vector3());
		Position.set(position);
		Recalculate();
		return this;
	}

	public final Vector3 getScale() {
		return Scale;
	}

	public ComponentPoint setScale(Vector3 scale) {
		updateChildren(new Vector3(), new Vector3(), new Vector3(scale).sub(Scale));
		Scale.set(scale);
		Recalculate();
		return this;
	}

	public final Vector3 getRotation() {
		return Rotation;
	}

	public ComponentPoint setRotation(Vector3 rotation) {
		updateChildren(new Vector3(), new Vector3(rotation).sub(Rotation), new Vector3());
		Rotation.set(rotation);
		Recalculate();
		return this;
	}

	public final Matrix4 getMatrix() {
		return comb;
	}

	public ComponentPoint setScale(float sc) {
		Scale.x = sc;
		Scale.y = sc;
		Scale.z = sc;
		Recalculate();
		return this;
	}

	public final float getScaleX() {
		return Scale.x;
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component").attribute("_Type", _Type.getName()).attribute("Position.x", Position.x)
				.attribute("Position.y", Position.y).attribute("Position.z", Position.z).attribute("Scale.x", Scale.x)
				.attribute("Scale.y", Scale.y).attribute("Scale.z", Scale.z).attribute("Rotation.x", Rotation.x)
				.attribute("Rotation.y", Rotation.y).attribute("Rotation.z", Rotation.z).pop();
	}

	@Override
	protected void Load(Element element) throws Exception {
		Position.x = element.getFloat("Position.x");
		Position.y = element.getFloat("Position.y");
		Position.z = element.getFloat("Position.z");
		Scale.x = element.getFloat("Scale.x");
		Scale.y = element.getFloat("Scale.y");
		Scale.z = element.getFloat("Scale.z");
		Rotation.x = element.getFloat("Rotation.x");
		Rotation.y = element.getFloat("Rotation.y");
		Rotation.z = element.getFloat("Rotation.z");
		Recalculate();
	}
}
