package com.ngeen.component.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.component.ComponentBase;
import com.ngeen.component.ComponentFactory;
import com.ngeen.component.ComponentPoint;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.ComponentSpokesman;
import com.ngeen.entity.Entity;

public class ComponentUIButton extends ComponentUILayout {
    private Button _Button;

    public ComponentUIButton(Ngeen ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
        super(ng, ent, factory, _ComponentSpokesman);
        click = true;
        ButtonStyle style = new ButtonStyle();
        Button but = new Button(style);
        _Button = but;
        _Button.setName(getOwner().getName());
    }

    @Override
    public void notifyWithComponent(ComponentPoint point) {
        _Button.setPosition(point.getPosition().x - _Button.getWidth() / 2,
                point.getPosition().y - _Button.getHeight() / 2);
        _Button.setZIndex(_Depth);
        if (point.getScale().isZero() && point.getRotation().isZero()) {
            _Button.setTransform(false);
        } else {
            _Button.setTransform(true);
            _Button.setScale(point.getScale().x, point.getScale().y);
            _Button.setRotation(point.getRotation().z);
        }
    }

    @Override
    public ComponentUIButton remove() {
        getOwner().removeComponent(ComponentUIWidget.class);
        _Owner.removeComponent(this.getClass(), Id);
        return this;
    }

    @Override
    protected void add(ComponentUIBase comp) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void del(ComponentUIBase comp) {
        // TODO Auto-generated method stub

    }

    @Override
    protected WidgetGroup get() {
        return _Button;
    }

    @Override
    protected ComponentBase Load(Element element) throws Exception {
        return this;
    }

    @Override
    protected void Save(XmlWriter element) throws Exception {
        element.element("Component").attribute("Type", this.getClass().getName()).pop();
    }

    @Override
    protected void swap(ComponentUIBase a, ComponentUIBase b) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void visitComponent(ComponentBase component, ComponentFactory factory) {
        factory.callComponentNotify(this, component);
    }
}
