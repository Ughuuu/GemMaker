package com.ngeen.component;

import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentUIWidget extends ComponentBase{
	protected Widget _Widget;
	private Class<?> _Type;
	
	public ComponentUIWidget(Ngeen ng, Entity ent) {
		super(ng, ent);
		// TODO Auto-generated constructor stub
	}

	public void act(float act) {
		_Widget.act(act);
	}
	
	@Override
	protected void Save(XmlWriter element) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void Load(Element element) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
