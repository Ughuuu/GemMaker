package com.ngeen.component;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.ComponentSpokesman;
import com.ngeen.entity.Entity;

import java.util.List;

public class ComponentPoint extends ComponentBase {
    private final Quaternion Rotation;
    private final Vector3 Position, Scale;
    private final Vector3 Up, Left, Forward;
    private static final Vector3 Identity = new Vector3(1,1,1);
    private static final Quaternion IdentityRotation = new Quaternion();
    private Matrix4 comb = new Matrix4();

    public ComponentPoint(Ngeen ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
        super(ng, ent, factory, _ComponentSpokesman);
        Position = new Vector3();
        Scale = new Vector3(1, 1, 1);
        Rotation = new Quaternion();
        Up = new Vector3();
        Left = new Vector3();
        Forward = new Vector3();
        Recalculate();
    }

    public Vector3 getForward() {
        return Forward.cpy();
    }

    public Vector3 getLeft() {
        return Left.cpy();
    }

    public final Matrix4 getMatrix() {
        return comb.cpy();
    }

    public Vector3 getPosition() {
        return new Vector3(comb.val[12], comb.val[13], comb.val[14]);
    }

    public ComponentPoint setPosition(Vector3 position) {
        Position.set(position);
        Recalculate();
        return this;
    }

    public final Quaternion getQuaternion() {
        return comb.getRotation(IdentityRotation);
    }

    public final Vector3 getRotation() {
        Quaternion finalRot = comb.getRotation(IdentityRotation);
        float x = finalRot.getAngleAround(1,0,0);
        float y = finalRot.getAngleAround(0,1,0);
        float z = finalRot.getAngleAround(0,0,1);
        return new Vector3(x,y,z);
    }

    public ComponentPoint setRotation(Vector3 rotation) {
        Rotation.setEulerAngles(rotation.x, rotation.y, rotation.z);
        Recalculate();
        return this;
    }

    public ComponentPoint setRotation(Quaternion rotation) {
        Rotation.set(rotation);
        Recalculate();
        return this;
    }

    public final Vector3 getScale() {
        return comb.getScale(Identity);
    }

    public ComponentPoint setScale(float sc) {
        Scale.x = sc;
        Scale.y = sc;
        Scale.z = sc;
        Recalculate();
        return this;
    }

    public ComponentPoint setScale(Vector3 scale) {
        Scale.set(scale);
        Recalculate();
        return this;
    }

    public Vector3 getUp() {
        return Up;
    }

    @Override
    public void reset() {
        Position.set(0, 0, 0);
        Scale.set(1, 1, 1);
        Rotation.setEulerAnglesRad(0, 0, 0);
        Recalculate();
    }

    private void makeRelativePosition() {
        if (_Owner.hasParent()) {
            Matrix4 matcpy = new Matrix4(_Owner.getParent().getComponent(ComponentPoint.class).getMatrix());
            comb = matcpy.mul(comb);
        }
    }

    private void Recalculate() {
        comb.set(Position.x, Position.y, Position.z, Rotation.x, Rotation.y, Rotation.z, Rotation.w, Scale.x, Scale.y, Scale.z);
        makeRelativePosition();
        Left.set(comb.val[0], comb.val[1], comb.val[2]);
        Up.set(comb.val[4], comb.val[5], comb.val[6]);
        Forward.set(comb.val[8], comb.val[9], comb.val[10]);
        updateChildren();
        _ComponentFactory.notifyAllComponents(_Owner.getComponents(), this);
    }

    @Override
    protected ComponentBase Load(Element element) throws Exception {
        Position.x = element.getChildByName("Position").getFloat("X");
        Position.y = element.getChildByName("Position").getFloat("Y");
        Position.z = element.getChildByName("Position").getFloat("Z");
        Scale.x = element.getChildByName("Scale").getFloat("X");
        Scale.y = element.getChildByName("Scale").getFloat("Y");
        Scale.z = element.getChildByName("Scale").getFloat("Z");
        float rx = element.getChildByName("Rotation").getFloat("X");
        float ry = element.getChildByName("Rotation").getFloat("Y");
        float rz = element.getChildByName("Rotation").getFloat("Z");
        Rotation.setEulerAnglesRad(rx, ry, rz);
        Recalculate();
        return this;
    }

    @Override
    protected void notifyParented(Entity parent) {
        Recalculate();
    }

    @Override
    protected void notifyDeparented(Entity parent) {
        Recalculate();
    }

    @Override
    protected void Save(XmlWriter element) throws Exception {
        Vector3 rot = getRotation();
        element.element("Component").attribute("Type", _Type.getName()).element("Position")
                .attribute("X", Position.x).attribute("Y", Position.y).attribute("Z", Position.z).pop().element("Scale")
                .attribute("X", Scale.x).attribute("Y", Scale.y).attribute("Z", Scale.z).pop().element("Rotation")
                .attribute("X", rot.x).attribute("Y", rot.y).attribute("Z", rot.z).pop().pop();
    }

    private void updateChildren() {
        List<Entity> entities = getOwner().getChildren();
        for (Entity ent : entities) {
            ComponentPoint point = ent.getComponent(ComponentPoint.class);
            point.Recalculate();
        }
    }

    @Override
    protected void visitComponent(ComponentBase component, ComponentFactory factory) {
        component.notifyWithComponent(this);
    }
}
