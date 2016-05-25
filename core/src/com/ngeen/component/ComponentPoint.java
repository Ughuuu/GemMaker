package com.ngeen.component;

import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.ComponentSpokesman;
import com.ngeen.entity.Entity;

import java.util.List;

public class ComponentPoint extends ComponentBase {
    private final Vector3 Position, Scale, Rotation;
    private final Vector3 Up, Left, Forward;
    Matrix4 comb = new Matrix4();

    public ComponentPoint(Ngeen ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
        super(ng, ent, factory, _ComponentSpokesman);
        Position = new Vector3();
        Scale = new Vector3(1, 1, 1);
        Rotation = new Vector3();
        Up = new Vector3();
        Left = new Vector3();
        Forward = new Vector3();
        Recalculate();
    }

    public Vector3 getForward() {
        return Forward;
    }

    public Vector3 getLeft() {
        return Left;
    }

    public final Matrix4 getMatrix() {
        return comb;
    }

    public Vector3 getPosition() {
        return Position;
    }

    public ComponentPoint setPosition(Vector3 position) {
        // updateChildren(new Vector3(position).sub(Position), new Vector3(),
        // new Vector3());
        Position.set(position);
        Recalculate();
        return this;
    }

    public final Quaternion getQuaternion() {
        return new Quaternion().setEulerAngles(Rotation.x, Rotation.y, Rotation.z);
    }

    public final Vector3 getRotation() {
        return Rotation;
    }

    public ComponentPoint setRotation(Vector3 rotation) {
        // updateChildren(new Vector3(), new Vector3(rotation).sub(Rotation),
        // new Vector3());
        Rotation.set(rotation);
        Recalculate();
        return this;
    }

    public final Vector3 getScale() {
        return Scale;
    }

    public ComponentPoint setScale(float sc) {
        // updateChildren(new Vector3(), new Vector3(), new
        // Vector3(sc,sc,sc).sub(Scale));
        Scale.x = sc;
        Scale.y = sc;
        Scale.z = sc;
        Recalculate();
        return this;
    }

    public ComponentPoint setScale(Vector3 scale) {
        // updateChildren(new Vector3(), new Vector3(), new
        // Vector3(scale).sub(Scale));
        Scale.set(scale);
        Recalculate();
        return this;
    }

    public final float getScaleX() {
        return Scale.x;
    }

    public Vector3 getUp() {
        return Up;
    }

    @Override
    public void reset() {
        Position.set(0, 0, 0);
        Scale.set(1, 1, 1);
        Rotation.set(0, 0, 0);
        Recalculate();
    }

    private void makeRelativePosition() {
        if (_Owner.hasParent()) {
            Matrix4 matcpy = new Matrix4(_Owner.getParent().getComponent(ComponentPoint.class).getMatrix());
            comb = matcpy.mul(comb);
            // comb.mul(_Owner.getParent().getComponent(ComponentPoint.class).getMatrix());
        }
    }

    private void Recalculate() {
        Quaternion q = new Quaternion().setEulerAngles(Rotation.x, Rotation.y, Rotation.z);
        comb.set(Position.x, Position.y, Position.z, q.x, q.y, q.z, q.w, Scale.x, Scale.y, Scale.z);
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
        Rotation.x = element.getChildByName("Rotation").getFloat("X");
        Rotation.y = element.getChildByName("Rotation").getFloat("Y");
        Rotation.z = element.getChildByName("Rotation").getFloat("Z");
        Recalculate();
        return this;
    }

    @Override
    protected void notiftyParented(Entity parent) {
        Recalculate();
    }

    @Override
    protected void notifyDeparented(Entity parent) {
        Recalculate();
    }

    @Override
    protected void Save(XmlWriter element) throws Exception {
        element.element("Component").attribute("Type", _Type.getName()).element("Position")
                .attribute("X", Position.x).attribute("Y", Position.y).attribute("Z", Position.z).pop().element("Scale")
                .attribute("X", Scale.x).attribute("Y", Scale.y).attribute("Z", Scale.z).pop().element("Rotation")
                .attribute("X", Rotation.x).attribute("Y", Rotation.y).attribute("Z", Rotation.z).pop().pop();
    }

    protected void updateChildren() {
        List<Entity> entities = getOwner().getChildren();
        for (Entity ent : entities) {
            ComponentPoint point = ent.getComponent(ComponentPoint.class);
            point.Recalculate();
        }
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

    @Override
    protected void visitComponent(ComponentBase component, ComponentFactory factory) {
        component.notifyWithComponent(this);
    }
}
