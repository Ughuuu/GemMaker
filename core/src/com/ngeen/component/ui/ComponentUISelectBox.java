package com.ngeen.component.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentUISelectBox extends ComponentUIWidget {
	private boolean _Saved = false;
	private SelectBox _SelectBox;
	
	public ComponentUISelectBox(Ngeen ng, Entity ent) {
		super(ng, ent);
		SelectBoxStyle style = new SelectBoxStyle();
		_SelectBox = new SelectBox(style);
		getOwner().addSuperComponent((ComponentUIWidget) this);
	}

	@Override
	public ComponentUISelectBox remove() {
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
		return _SelectBox;
	}
}
