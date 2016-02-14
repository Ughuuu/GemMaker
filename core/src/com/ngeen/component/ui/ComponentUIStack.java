package com.ngeen.component.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentUIStack extends ComponentUILayout {
	private boolean _Saved = false;
	private Stack _Stack;
	
	public ComponentUIStack(Ngeen ng, Entity ent) {
		super(ng, ent);
		_Stack = new Stack();
		getOwner().addSuperComponent((ComponentUILayout) this);
	}

	@Override
	public ComponentUIStack remove() {
		getOwner().removeComponent(ComponentUILayout.class);
		_Owner.removeComponent(this.getClass(), Id);
		return this;
	}

	@Override
	protected void Load(Element element) throws Exception {
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		if (_Saved) {
			_Saved = false;
			return;
		}
		_Saved = true;
		element.element("Component").attribute("_Type", this.getClass().getName()).pop();
	}

	@Override
	protected WidgetGroup get() {
		return _Stack;
	}

	@Override
	protected void add(ComponentUIBase comp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void del(ComponentUIBase comp) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void swap(ComponentUIBase a, ComponentUIBase b) {
		// TODO Auto-generated method stub
		
	}
}
