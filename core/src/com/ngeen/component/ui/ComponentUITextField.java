package com.ngeen.component.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentUITextField extends ComponentUIWidget {
	private boolean _Saved = false;

	public ComponentUITextField(Ngeen ng, Entity ent) {
		super(ng, ent);
		TextFieldStyle style = new TextFieldStyle();
		BitmapFont font = (BitmapFont) _Ng.Loader.getAsset("LoadScene/fonts/impact.fnt").getData();
		style.font = font;
		_Widget = new TextField("Text", style);
		getOwner().addSuperComponent((ComponentUIWidget) this);
	}

	@Override
	public void act(float delta) {
		_Widget.act(delta);
	}

	@Override
	public ComponentUITextField remove() {
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

	@Override
	protected void Load(Element element) throws Exception {
	}
}
