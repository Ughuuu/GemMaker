package com.gem.component.ui;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.gem.component.ComponentBase;
import com.gem.component.ComponentFactory;
import com.gem.engine.Gem;
import com.gem.entity.ComponentSpokesman;
import com.gem.entity.Entity;

public class ComponentUIImageButton extends ComponentUILayout {
    private ImageButton _ImageButton;
    private boolean _Saved = false;

    public ComponentUIImageButton(Gem ng, Entity ent, ComponentFactory factory,
                                  ComponentSpokesman _ComponentSpokesman) {
        super(ng, ent, factory, _ComponentSpokesman);
        ImageButtonStyle style = new ImageButtonStyle();
        _ImageButton = new ImageButton(style);
    }

    @Override
    public ComponentUIImageButton remove() {
        getOwner().removeComponent(ComponentUIWidget.class);
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
        // TODO Auto-generated method stub
        return _ImageButton;
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
