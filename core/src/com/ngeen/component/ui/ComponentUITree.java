package com.ngeen.component.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.ui.Tree.TreeStyle;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.component.ComponentBase;
import com.ngeen.component.ComponentFactory;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.ComponentSpokesman;
import com.ngeen.entity.Entity;

public class ComponentUITree extends ComponentUILayout {
    private boolean _Saved = false;
    private Tree _Tree;

    public ComponentUITree(Ngeen ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
        super(ng, ent, factory, _ComponentSpokesman);
        TreeStyle style = new TreeStyle();
        _Tree = new Tree(style);
    }

    @Override
    public ComponentUITree remove() {
        getOwner().removeComponent(ComponentUILayout.class);
        _Owner.removeComponent(this.getClass(), Id);
        return this;
    }

    @Override
    protected WidgetGroup get() {
        return _Tree;
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
    protected void visitComponent(ComponentBase component, ComponentFactory factory) {
        factory.callComponentNotify(this, component);
    }
}
