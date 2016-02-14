package com.ngeen.component.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentUIImage extends ComponentUIWidget {
	private boolean _Saved = false;
	private Image _Image;
	public ComponentUIImage(Ngeen ng, Entity ent) {
		super(ng, ent);
		_Image = new Image();
		getOwner().addSuperComponent((ComponentUIWidget) this);
	}

	@Override
	public ComponentUIImage remove() {
		getOwner().removeComponent(ComponentUIWidget.class);
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
	protected Actor getActor() {
		return _Image;
	}
}
