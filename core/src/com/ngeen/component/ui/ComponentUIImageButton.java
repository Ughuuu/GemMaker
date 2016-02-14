package com.ngeen.component.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentUIImageButton extends ComponentUILayout {
	private boolean _Saved = false;
	private ImageButton _ImageButton;

	public ComponentUIImageButton(Ngeen ng, Entity ent) {
		super(ng, ent);
		ImageButtonStyle style = new ImageButtonStyle();
		_ImageButton = new ImageButton(style);
		getOwner().addSuperComponent((ComponentUILayout) this);
	}

	@Override
	public ComponentUIImageButton remove() {
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
	protected WidgetGroup get() {
		// TODO Auto-generated method stub
		return _ImageButton;
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
