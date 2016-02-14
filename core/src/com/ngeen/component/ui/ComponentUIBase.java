package com.ngeen.component.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.component.ComponentBase;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public abstract class ComponentUIBase extends ComponentBase{

	protected boolean click = false;
	
	public ComponentUIBase(Ngeen ng, Entity ent) {
		super(ng, ent);
	}

	protected void add(ComponentUIBase comp){}
	
	protected void del(ComponentUIBase comp){}
	
	protected void swap(ComponentUIBase a, ComponentUIBase b){}
	
	protected abstract Actor getActor();

}
