package com.gem.component.ui;

import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.gem.component.ComponentBase;
import com.gem.component.ComponentFactory;
import com.gem.engine.Gem;
import com.gem.entity.ComponentSpokesman;
import com.gem.entity.Entity;

public class ComponentUIVerticalGroup extends ComponentUILayout {
    private boolean _Saved = false;
    private VerticalGroup _VerticalGroup;

    public ComponentUIVerticalGroup(Gem ng, Entity ent, ComponentFactory factory,
                                    ComponentSpokesman _ComponentSpokesman) {
        super(ng, ent, factory, _ComponentSpokesman);
        _VerticalGroup = new VerticalGroup();
    }

    @Override
    public ComponentUIVerticalGroup remove() {
        getOwner().removeComponent(ComponentUILayout.class);
        owner.removeComponent(this.getClass(), id);
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
        return _VerticalGroup;
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
