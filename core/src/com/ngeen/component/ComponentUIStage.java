package com.ngeen.component;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentUIStage extends ComponentBase{
	private Stage _Stage;
	
	public ComponentUIStage(Ngeen ng, Entity ent) {
		super(ng, ent);
	}
	
	public void setStage(Viewport view, SpriteBatch batch, InputMultiplexer multiplexer){
		_Stage = new Stage(view, batch);
		multiplexer.addProcessor(_Stage);
	}

	public ComponentUIStage act(){
		_Stage.act();
		return this;
	}
	
	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component")
		.attribute("_Type", this.getClass().getName())
		.pop();
	}

	@Override
	protected void Load(Element element) throws Exception {
	}
}
