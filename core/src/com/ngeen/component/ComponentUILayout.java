package com.ngeen.component;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentUILayout extends ComponentBase{
	protected WidgetGroup _Layout;
	
	public ComponentUILayout(Ngeen ng, Entity ent) {
		super(ng, ent);
	}
	
	public void act(float act) {
		_Layout.act(act);
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		//Don't save it, regenerate it.
	}

	@Override
	protected void Load(Element element) throws Exception {
		//Don't load it, regenerate it.
	}
}
