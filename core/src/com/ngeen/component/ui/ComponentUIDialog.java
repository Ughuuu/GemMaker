package com.ngeen.component.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentUIDialog extends ComponentUILayout {
	private boolean _Saved = false;

	public ComponentUIDialog(Ngeen ng, Entity ent) {
		super(ng, ent);
		WindowStyle style = new WindowStyle();
		_Layout = new Dialog("Text", style);
		getOwner().addSuperComponent((ComponentUILayout) this);
	}

	@Override
	public void act(float delta) {
		_Layout.act(delta);
	}

	@Override
	protected void Load(Element element) throws Exception {
	}

	@Override
	public ComponentUIDialog remove() {
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
