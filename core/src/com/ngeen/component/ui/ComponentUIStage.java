package com.ngeen.component.ui;

import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.ngeen.component.ComponentBase;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentUIStage extends ComponentBase {
	protected Stage _Stage;

	public ComponentUIStage(Ngeen ng, Entity ent) {
		super(ng, ent);
		_Ng.UIBuilder.createStage(this);
	}

	public ComponentUIStage act() {
		_Stage.act();
		return this;
	}

	@Override
	protected void Load(Element element) throws Exception {
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component").attribute("_Type", _Type.getName()).pop();
	}

	public void setStage(Viewport view, SpriteBatch batch, InputMultiplexer multiplexer) {
		_Stage = new Stage(view, batch);
		multiplexer.addProcessor(_Stage);
	}
}
