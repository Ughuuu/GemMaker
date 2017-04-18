package com.gem.component;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.gem.engine.Gem;
import com.gem.entity.ComponentSpokesman;
import com.gem.entity.Entity;

public class ComponentVariable extends ComponentBase {
    public ComponentVariable(Gem ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
        super(ng, ent, factory, _ComponentSpokesman);
    }

    @Override
    protected ComponentBase Load(Element element) throws Exception {
        // TODO Auto-generated method stub
        return this;
    }

    @Override
    protected void Save(XmlWriter element) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    protected void visitComponent(ComponentBase component, ComponentFactory factory) {
        component.notifyWithComponent(this);
    }

}
