package com.ngeen.component.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentUIList extends ComponentUIWidget {
	private boolean _Saved = false;
	private List _List;
	
	public ComponentUIList(Ngeen ng, Entity ent) {
		super(ng, ent);
		ListStyle style = new ListStyle();
		_List = new List(style);
		getOwner().addSuperComponent((ComponentUIWidget) this);
	}

	@Override
	public ComponentUIList remove() {
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
		return _List;
	}
}
