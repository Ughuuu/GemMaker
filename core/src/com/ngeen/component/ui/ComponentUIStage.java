package com.ngeen.component.ui;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ngeen.component.ComponentBase;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentUIStage extends ComponentUIBase {

	protected Stage _Stage;

	public ComponentUIStage(Ngeen ng, Entity ent) {
		super(ng, ent);
		_Ng.UIBuilder.createStage(this);
	}

	public ComponentUIStage act() {
		_Stage.act();
		_Stage.draw();
		return this;
	}

	public void setStage(Viewport view, SpriteBatch batch, InputMultiplexer multiplexer) {
		_Stage = new Stage(view, batch);
		_Stage.setDebugAll(true);
		multiplexer.addProcessor(_Stage);
	}

	@Override
	protected void Load(Element element) throws Exception {
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component").attribute("_Type", _Type.getName()).pop();
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
	protected void add(ComponentUIBase comp) {
		_Stage.addActor(comp.getActor());
	}

	@Override
	protected void del(ComponentUIBase comp) {
		_Stage.getActors().removeValue(comp.getActor(), true);
	}

	@Override
	protected void swap(ComponentUIBase a, ComponentUIBase b) {
		//not important, will use z index... so this will be resorted i hope
		//don't do anything here.
	}
}
