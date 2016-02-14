package com.ngeen.component.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentUITextField extends ComponentUIWidget {
	private boolean _Saved = false;
	private TextField _TextField;
	
	public ComponentUITextField(Ngeen ng, Entity ent) {
		super(ng, ent);
		TextFieldStyle style = new TextFieldStyle();
		BitmapFont font = (BitmapFont) _Ng.Loader.getAsset("LoadScene/fonts/impact.fnt").getData();
		style.font = font;
		_TextField = new TextField("Text", style);
		getOwner().addSuperComponent((ComponentUIWidget) this);
	}

	@Override
	public ComponentUITextField remove() {
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
		return _TextField;
	}
}
