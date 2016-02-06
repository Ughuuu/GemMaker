package com.ngeen.component.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentUITextButton extends ComponentUILayout {
	private boolean _Saved = false;

	public ComponentUITextButton(Ngeen ng, Entity ent) {
		super(ng, ent);
		TextButtonStyle style = new TextButtonStyle();
		BitmapFont font = (BitmapFont) _Ng.Loader.getAsset("LoadScene/fonts/impact.fnt").getData();
		style.font = font;
		TextButton but = new TextButton("Text", style);
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
	public ComponentUITextButton remove() {
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
