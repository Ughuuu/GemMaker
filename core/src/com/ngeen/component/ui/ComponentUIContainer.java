package com.ngeen.component.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.component.ComponentBase;
import com.ngeen.component.ComponentFactory;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.ComponentSpokesman;
import com.ngeen.entity.Entity;

public class ComponentUIContainer extends ComponentUILayout {
    private Container _Container;
    private boolean _Saved = false;

    public ComponentUIContainer(Ngeen ng, Entity ent, ComponentFactory factory,
                                ComponentSpokesman _ComponentSpokesman) {
        super(ng, ent, factory, _ComponentSpokesman);
        _Container = new Container();
    }

    @Override
    public ComponentUIContainer remove() {
        getOwner().removeComponent(ComponentUILayout.class);
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
        return _Container;
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
