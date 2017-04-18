package com.gem.component.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.gem.component.ComponentBase;
import com.gem.component.ComponentFactory;
import com.gem.engine.Gem;
import com.gem.entity.ComponentSpokesman;
import com.gem.entity.Entity;

public class ComponentUIButtonGroup extends ComponentUIBase {
    private ButtonGroup<Button> _ButtonGroup;

    public ComponentUIButtonGroup(Gem ng, Entity ent, ComponentFactory factory,
                                  ComponentSpokesman _ComponentSpokesman) {
        super(ng, ent, factory, _ComponentSpokesman);
        _ButtonGroup = new ButtonGroup<Button>();
    }

    public void set() {
        Array<Button> arr = _ButtonGroup.getAllChecked();
    }

    @Override
    protected void add(ComponentUIBase comp) {
        if (comp.click)
            _ButtonGroup.add((Button) comp.getActor());
    }

    @Override
    protected void del(ComponentUIBase comp) {
        if (comp.click)
            _ButtonGroup.remove((Button) comp.getActor());
    }

    /**
     * This one has no actor. It's a logic stuff.
     */
    @Override
    protected Actor getActor() {
        return null;
    }

    @Override
    protected ComponentBase Load(Element element) throws Exception {
        return this;
    }

    @Override
    protected void reinit() {

    }

    @Override
    protected void Save(XmlWriter element) throws Exception {
        element.element("Component").attribute("Type", type.getName()).pop();
    }

    /**
     * Does it even matter the order?
     */
    @Override
    protected void swap(ComponentUIBase a, ComponentUIBase b) {
    }

    @Override
    protected void visitComponent(ComponentBase component, ComponentFactory factory) {
        factory.callComponentNotify(this, component);
    }
}
