package com.ngeen.component.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.component.ComponentBase;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentUIButtonGroup extends ComponentUIBase {
	private ButtonGroup _ButtonGroup;

	public ComponentUIButtonGroup(Ngeen ng, Entity ent) {
		super(ng, ent);
		_ButtonGroup = new ButtonGroup();
	}
	
	public void set(){
		Array arr = _ButtonGroup.getAllChecked();
	}

	@Override
	protected void Load(Element element) throws Exception {
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component").attribute("_Type", _Type.getName()).pop();
	}

	@Override
	protected void add(ComponentUIBase comp) {
		if(comp.click)
		_ButtonGroup.add((Button)comp.getActor());
	}

	@Override
	protected void del(ComponentUIBase comp) {
		if(comp.click)
		_ButtonGroup.remove((Button)comp.getActor());
	}

	/**
	 * Does it even matter the order?
	 */
	@Override
	protected void swap(ComponentUIBase a, ComponentUIBase b) {		
	}

	/**
	 * This one has no actor. It's a logic stuff.
	 */
	@Override
	protected Actor getActor() {
		return null;
	}
}
