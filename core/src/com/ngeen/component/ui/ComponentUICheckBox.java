package com.ngeen.component.ui;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentUICheckBox extends ComponentUILayout {
	private boolean _Saved = false;

	public ComponentUICheckBox(Ngeen ng, Entity ent) {
		super(ng, ent);
		CheckBoxStyle style = new CheckBoxStyle();
		CheckBox but = new CheckBox("Text", style);
		_Layout = but;
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
	public ComponentUICheckBox remove() {
		getOwner().removeComponent(ComponentUILayout.class);
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
