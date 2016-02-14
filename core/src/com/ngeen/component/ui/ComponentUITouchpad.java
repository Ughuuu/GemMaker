package com.ngeen.component.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad.TouchpadStyle;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentUITouchpad extends ComponentUIWidget {
	private boolean _Saved = false;
	private Touchpad _Touchpad;

	public ComponentUITouchpad(Ngeen ng, Entity ent) {
		super(ng, ent);
		TouchpadStyle style = new TouchpadStyle();
		_Touchpad = new Touchpad(50, style);
		getOwner().addSuperComponent((ComponentUIWidget) this);
	}

	@Override
	public ComponentUITouchpad remove() {
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
		return _Touchpad;
	}
}
