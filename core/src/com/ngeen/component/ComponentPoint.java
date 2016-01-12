package com.ngeen.component;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.ngeen.engine.Ngeen;

public class ComponentPoint extends ComponentBase {
	Matrix4 comb = new Matrix4();
	public boolean Update = false;
	
	public ComponentPoint(Ngeen ng) {
		super(ng);
		Position = new Vector3();
		Scale = new Vector3(1,1,1);
		Rotation = new Vector3();
	}

	private Vector3 Position, Scale, Rotation;

	private void Recalculate() {
		comb = comb.idt().scl(Scale).rotate(new Quaternion().setEulerAngles(Rotation.x, Rotation.y, Rotation.z)).translate(Position);
		Update = true;
	}
	
	public void Update(){
		Recalculate();
	}

	public Vector3 getPosition() {
		return Position;
	}

	public ComponentPoint setPosition(Vector3 position) {
		Position = position;
		Recalculate();
		return this;
	}

	public final Vector3 getScale() {
		return Scale;
	}

	public ComponentPoint setScale(Vector3 scale) {
		Scale = scale;
		Recalculate();
		return this;
	}

	public final Vector3 getRotation() {
		return Rotation;
	}

	public ComponentPoint setRotation(Vector3 rotation) {
		Rotation = rotation;
		Recalculate();
		return this;
	}
	
	public final Matrix4 getMatrix(){
		return comb;
	}
	
	public ComponentPoint setScale(float sc){
		Scale.x = sc;
		Scale.y = sc;
		Scale.z = sc;
		Recalculate();
		return this;
	}
	
	public final float getScaleX(){
		return Scale.x;
	}	

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component").
		attribute("_Type", "ComponentPoint").
		attribute("Position.x", Position.x).
		attribute("Position.y", Position.y).
		attribute("Position.z", Position.z).
		attribute("Scale.x", Scale.x).
		attribute("Scale.y", Scale.y).
		attribute("Scale.z", Scale.z).
		attribute("Rotation.x", Rotation.x).
		attribute("Rotation.y", Rotation.y).
		attribute("Rotation.z", Rotation.z)
		       .pop();
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
		Update();
	}
}
