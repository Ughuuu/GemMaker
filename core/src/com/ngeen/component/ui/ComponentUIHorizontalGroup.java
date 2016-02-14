package com.ngeen.component.ui;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentUIHorizontalGroup extends ComponentUILayout {
	private boolean _Saved = false;
	private HorizontalGroup _HorizontalGroup;
	
	public ComponentUIHorizontalGroup(Ngeen ng, Entity ent) {
		super(ng, ent);
		_HorizontalGroup = new HorizontalGroup();
		getOwner().addSuperComponent((ComponentUILayout) this);
	}

	@Override
	public ComponentUIHorizontalGroup remove() {
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
		return _HorizontalGroup;
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