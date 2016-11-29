package com.gem.component;

import java.util.List;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.linearmath.LinearMath;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.gem.engine.Gem;
import com.gem.entity.ComponentSpokesman;
import com.gem.entity.Entity;

public class ComponentPoint extends ComponentBase {
	private static final Quaternion IdentityRotation = new Quaternion();
	private Matrix4 matLocal = new Matrix4();
	private Matrix4 matGlobal = new Matrix4();
	private final Vector3[] cache = new Vector3[15];

	public ComponentPoint(Gem ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
		super(ng, ent, factory, _ComponentSpokesman);
		for (int i = 0; i < cache.length; i++) {
			cache[i] = new Vector3();
		}
		recalculateFromParent();
	}

	public Vector3 getForward() {
		return getForward(matGlobal, 1);
	}

	public Vector3 getLeft() {
		return getLeft(matGlobal, 2);
	}

	public final Matrix4 getLocalMatrix() {
		return matLocal.cpy();
	}

	public final Matrix4 getMatrix() {
		return matGlobal.cpy();
	}

	public Vector3 getPosition() {
		return getPosition(matGlobal, 4);
	}

	public final Quaternion getQuaternion() {
		return getQuaternion(matGlobal);
	}

	public Vector3 getRelativeForward() {
		return getForward(matLocal, 8);
	}

	public Vector3 getRelativeLeft() {
		return getLeft(matLocal, 9);
	}

	public Vector3 getRelativePosition() {
		return getPosition(matLocal, 11);
	}

	public final Quaternion getRelativeQuaternion() {
		return getQuaternion(matLocal);
	}

	// GLOBAL SPACE

	public Vector3 getRelativeRight() {
		return getRight(matLocal, 10);
	}

	public final Vector3 getRelativeRotation() {
		return getRotation(matLocal, 12);
	}

	public final Vector3 getRelativeScale() {
		return getScale(matLocal, 13);
	}

	public Vector3 getRelativeUp() {
		return getUp(matLocal, 7);
	}

	public Vector3 getRight() {
		return getRight(matGlobal, 3);
	}

	public final Vector3 getRotation() {
		return getRotation(matGlobal, 5);
	}

	public final Vector3 getScale() {
		return getScale(matGlobal, 6);
	}

	public Vector3 getUp() {
		return getUp(matGlobal, 0);
	}

	// LOCAL SPACE

	@Override
	public void reset() {
		matGlobal.idt();
		matLocal.idt();
		recalculateFromParent();
	}

	public ComponentPoint setPosition(Vector3 position) {
		recalculateGlobalPos(position);
		return this;
	}

	public ComponentPoint setRotation(Quaternion rotation) {
		recalculateGlobal(getPosition(), cache[14].set(rotation.getPitch(), rotation.getYaw(), rotation.getRoll()), getScale());
		return this;
	}

	public ComponentPoint setRotation(Vector3 rotation) {
		recalculateGlobal(getPosition(), rotation, getScale());
		return this;
	}

	public ComponentPoint setScale(float sc) {
		recalculateGlobal(getPosition(), getRotation(), cache[14].set(sc, sc, sc));
		return this;
	}

	public ComponentPoint setScale(Vector3 scale) {
		recalculateGlobal(getPosition(), getRotation(), scale);
		return this;
	}

	public ComponentPoint setRelativePosition(Vector3 position) {
		recalculateFromParent(cache[14].setZero().add(getPosition()).sub(getRelativePosition()).add(position), getQuaternion(),
				getScale());
		return this;
	}

	public ComponentPoint setRelativeRotation(Quaternion rotation) {
		Quaternion inverseLocal = LinearMath.inverse(getRelativeQuaternion());
		recalculateFromParent(getPosition(), new Quaternion().set(getQuaternion()).mul(inverseLocal).mul(rotation), getScale());
		return this;
	}

	public ComponentPoint setRelativeRotation(Vector3 rotation) {
		return setRelativeRotation(new Quaternion().setEulerAngles(rotation.x, rotation.y, rotation.z));
	}

	public ComponentPoint setRelativeScale(float sc) {
		recalculateFromParent(cache[14].setZero().add(getPosition()).sub(getRelativePosition()).add(cache[14].set(sc, sc, sc)),
				getQuaternion(), getScale());
		return this;
	}

	public ComponentPoint setRelativeScale(Vector3 scale) {
		recalculateFromParent(cache[14].setZero().add(getPosition()).sub(getRelativePosition()).add(scale), getQuaternion(),
				getScale());
		return this;
	}

	private Vector3 getForward(Matrix4 mat, int cacheLvl) {
		return cache[cacheLvl].set(mat.val[8], mat.val[9], mat.val[10]);
	}

	private Vector3 getLeft(Matrix4 mat, int cacheLvl) {
		return cache[cacheLvl].set(mat.val[0], mat.val[1], mat.val[2]);
	}

	private Vector3 getPosition(Matrix4 mat, int cacheLvl) {
		return cache[cacheLvl].set(mat.val[12], mat.val[13], mat.val[14]);
	}

	private final Quaternion getQuaternion(Matrix4 mat) {
		return mat.getRotation(IdentityRotation);
	}

	private Vector3 getRight(Matrix4 mat, int cacheLvl) {
		return cache[cacheLvl].set(mat.val[0], mat.val[1], mat.val[2]).scl(-1);
	}

	private final Vector3 getRotation(Matrix4 mat, int cacheLvl) {
		Quaternion finalRot = mat.getRotation(IdentityRotation);
		float x = finalRot.getPitch();
		float y = finalRot.getYaw();
		float z = finalRot.getRoll();
		if(Owner.getName().equals("P")){
			System.out.println(x + " " + y + " " + z);
		}
		return cache[cacheLvl].set(x, y, z);
	}

	private final Vector3 getScale(Matrix4 mat, int cacheLvl) {
		return mat.getScale(cache[cacheLvl]);
	}

	private Vector3 getUp(Matrix4 mat, int cacheLvl) {
		return cache[cacheLvl].set(mat.val[4], mat.val[5], mat.val[6]);
	}

	private void recalculateFromParent() {
		if (Owner.hasParent()) {
			Matrix4 matcpy = new Matrix4(Owner.getParent().getComponent(ComponentPoint.class).getMatrix());
			matGlobal = matcpy.mul(matLocal);
		}else{
			matGlobal = matLocal;
		}
		updateChildren();
		ComponentFactory.notifyAllComponents(Owner.getComponents(), this);
	}

	private void recalculateFromParent(Vector3 pos, Quaternion rot, Vector3 scl) {
		matLocal.set(pos.x, pos.y, pos.z, rot.x, rot.y, rot.z, rot.w, scl.x, scl.y, scl.z);
		if (Owner.hasParent()) {
			Matrix4 matcpy = new Matrix4(Owner.getParent().getComponent(ComponentPoint.class).getMatrix());
			matGlobal = matcpy.mul(matLocal);
		}else{
			matGlobal = matLocal.cpy();
		}
		updateChildren();
		ComponentFactory.notifyAllComponents(Owner.getComponents(), this);
	}
	
	private void recalculateGlobal(Vector3 pos, Vector3 rot, Vector3 scl) {
		matGlobal.setFromEulerAngles(rot.x, rot.y, rot.z).scl(scl).trn(pos);
		getRotation();
		//matGlobal.set(pos.x, pos.y, pos.z, rot.x, rot.y, rot.z, rot.w, scl.x, scl.y, scl.z);
		if (Owner.hasParent()) {
			Matrix4 matcpy = new Matrix4(Owner.getParent().getComponent(ComponentPoint.class).getMatrix());
			matLocal = matGlobal.cpy().mul(matcpy.inv());
		}else{
			matLocal = matGlobal.cpy();
		}
		updateChildren();
		ComponentFactory.notifyAllComponents(Owner.getComponents(), this);
	}
	
	private void recalculateGlobalPos(Vector3 pos) {
		matGlobal.setTranslation(pos.x, pos.y, pos.z);
		if (Owner.hasParent()) {
			Matrix4 matcpy = new Matrix4(Owner.getParent().getComponent(ComponentPoint.class).getMatrix());
			matLocal = matGlobal.cpy().mul(matcpy.inv());
		}else{
			matLocal = matGlobal.cpy();
		}
		updateChildren();
		ComponentFactory.notifyAllComponents(Owner.getComponents(), this);
	}
	
	private void updateChildren() {
		List<Entity> entities = getOwner().getChildren();
		for (Entity ent : entities) {
			ComponentPoint point = ent.getComponent(ComponentPoint.class);
			point.recalculateFromParent();
		}
	}

	@Override
	protected ComponentBase Load(Element element) throws Exception {
		cache[4].x = element.getChildByName("Position").getFloat("X");
		cache[4].y = element.getChildByName("Position").getFloat("Y");
		cache[4].z = element.getChildByName("Position").getFloat("Z");
		cache[5].x = element.getChildByName("Rotation").getFloat("X");
		cache[5].y = element.getChildByName("Rotation").getFloat("Y");
		cache[5].z = element.getChildByName("Rotation").getFloat("Z");
		cache[6].x = element.getChildByName("Scale").getFloat("X");
		cache[6].y = element.getChildByName("Scale").getFloat("Y");
		cache[6].z = element.getChildByName("Scale").getFloat("Z");
		recalculateGlobal(cache[4], cache[5], cache[6]);
		return this;
	}

	@Override
	protected void notifyDeparented(Entity parent) {
		recalculateFromParent();
	}

	@Override
	protected void notifyParented(Entity parent) {
		recalculateFromParent();
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		Vector3 rot = getRotation();
		Vector3 pos = getPosition();
		Vector3 scl = getScale();
		element.element("Component").attribute("Type", Type.getName()).element("Position").attribute("X", pos.x)
				.attribute("Y", pos.y).attribute("Z", pos.z).pop().element("Scale").attribute("X", scl.x)
				.attribute("Y", scl.y).attribute("Z", scl.z).pop().element("Rotation").attribute("X", rot.x)
				.attribute("Y", rot.y).attribute("Z", rot.z).pop().pop();
	}

	@Override
	protected void visitComponent(ComponentBase component, ComponentFactory factory) {
		component.notifyWithComponent(this);
	}
}
