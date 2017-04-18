package com.gem.component.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.gem.component.ComponentBase;
import com.gem.component.ComponentFactory;
import com.gem.engine.Gem;
import com.gem.entity.ComponentSpokesman;
import com.gem.entity.Entity;

public class ComponentUISlider extends ComponentUIWidget {
    private boolean _Saved = false;
    private Slider _Slider;

    public ComponentUISlider(Gem ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
        super(ng, ent, factory, _ComponentSpokesman);
        SliderStyle style = new SliderStyle();
        _Slider = new Slider(0, 1, 0.1f, true, style);
    }

    @Override
    public ComponentUISlider remove() {
        getOwner().removeComponent(ComponentUIWidget.class);
        owner.removeComponent(this.getClass(), id);
        return this;
    }

    @Override
    protected Actor getActor() {
        return _Slider;
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
