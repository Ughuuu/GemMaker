package com.gem.component.ui;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.gem.component.ComponentBase;
import com.gem.component.ComponentCamera;
import com.gem.component.ComponentFactory;
import com.gem.component.ComponentPoint;
import com.gem.engine.Gem;
import com.gem.entity.ComponentSpokesman;
import com.gem.entity.Entity;

public class ComponentUIStage extends ComponentUIBase {

    protected Stage stage;

    public ComponentUIStage(Gem ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
        super(ng, ent, factory, _ComponentSpokesman);
    }

    public ComponentUIStage act() {
        if(stage == null)
        	return this;
        stage.act();
        stage.draw();
        return this;
    }

    @Override
    protected void notifyWithComponent(ComponentCamera camera) {
    	componentSpokesman.getUIBuilder().createStage(this);
    }
    
    @Override
    protected void notifyParented(Entity parent){
        componentSpokesman.getUIBuilder().createStage(this);
    }

    public void setStage(Viewport view, SpriteBatch batch, InputMultiplexer multiplexer) {
        stage = new Stage(view, batch);
        stage.setDebugAll(true);
        multiplexer.addProcessor(stage);
    }

    @Override
    protected void add(ComponentUIBase comp) {
    	if(stage == null)
    		return;
        stage.addActor(comp.getActor());
        componentFactory.notifyAllComponents(owner.getComponents(), comp);
    }

    @Override
    protected void del(ComponentUIBase comp) {
    	if(stage == null)
    		return;
        stage.getActors().removeValue(comp.getActor(), true);
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
        element.element("Component").attribute("Type", type.getName()).pop();
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
