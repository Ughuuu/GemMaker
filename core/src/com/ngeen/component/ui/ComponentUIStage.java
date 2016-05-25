package com.ngeen.component.ui;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ngeen.component.ComponentBase;
import com.ngeen.component.ComponentFactory;
import com.ngeen.component.ComponentPoint;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.ComponentSpokesman;
import com.ngeen.entity.Entity;

public class ComponentUIStage extends ComponentUIBase {

    protected Stage _Stage;

    public ComponentUIStage(Ngeen ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
        super(ng, ent, factory, _ComponentSpokesman);
        _ComponentSpokesman.getUIBuilder().createStage(this);
    }

    public ComponentUIStage act() {
        try {
            // _Stage.getActors().items[4].setPosition(100, 100);
        } catch (Exception e) {

        }
        _Stage.act();
        _Stage.draw();
        return this;
    }

    @Override
    public void notifyWithComponent(ComponentPoint point) {
        // _Stage.getViewport().getCamera().view.set(point.getMatrix());
    }

    public void setStage(Viewport view, SpriteBatch batch, InputMultiplexer multiplexer) {
        _Stage = new Stage(view, batch);
        _Stage.setDebugAll(true);
        multiplexer.addProcessor(_Stage);
    }

    @Override
    protected void add(ComponentUIBase comp) {
        _Stage.addActor(comp.getActor());
        _ComponentFactory.notifyAllComponents(_Owner.getComponents(), comp);
    }

    @Override
    protected void del(ComponentUIBase comp) {
        _Stage.getActors().removeValue(comp.getActor(), true);
    }

    /**
     * Stage is not an actor..
     */
    @Override
    protected Actor getActor() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected ComponentBase Load(Element element) throws Exception {
        return this;
    }

    @Override
    protected void Save(XmlWriter element) throws Exception {
        element.element("Component").attribute("Type", _Type.getName()).pop();
    }

    @Override
    protected void swap(ComponentUIBase a, ComponentUIBase b) {
        // not important, will use z index... so this will be resorted i hope
        // don't do anything here.
    }

    @Override
    protected void visitComponent(ComponentBase component, ComponentFactory factory) {
        factory.callComponentNotify(this, component);
    }
}
