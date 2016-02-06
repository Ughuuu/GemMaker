package com.ngeen.component.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentUIImage extends ComponentUIWidget {
	private boolean _Saved = false;

	public ComponentUIImage(Ngeen ng, Entity ent) {
		super(ng, ent);
		_Widget = new Image();
		getOwner().addSuperComponent((ComponentUIWidget) this);
	}

	@Override
	public void act(float delta) {
		_Widget.act(delta);
	}

	@Override
	protected void Load(Element element) throws Exception {
	}

	@Override
	public ComponentUIImage remove() {
		getOwner().removeComponent(ComponentUIWidget.class);
		_Owner.removeComponent(this.getClass(), Id);
		return this;
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
}
