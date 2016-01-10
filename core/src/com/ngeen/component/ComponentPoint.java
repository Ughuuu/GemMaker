package com.ngeen.component;

import com.badlogic.gdx.math.Vector3;
import com.ngeen.engine.Ngeen;

public class ComponentPoint extends ComponentBase {
	public ComponentPoint(Ngeen ng) {
		super(ng);
	}

	private Vector3 Position, Scale, Rotation;

	private void Recalculate() {

	}

	public Vector3 getPosition() {
		return Position;
	}

	public void setPosition(Vector3 position) {
		Position = position;
		Recalculate();
	}

	public Vector3 getScale() {
		return Scale;
	}

	public void setScale(Vector3 scale) {
		Scale = scale;
		Recalculate();
	}

	public Vector3 getRotation() {
		return Rotation;
	}

	public void setRotation(Vector3 rotation) {
		Rotation = rotation;
		Recalculate();
	}
}
