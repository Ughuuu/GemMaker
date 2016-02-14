package com.ngeen.component.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.component.ComponentBase;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentUIWindow extends ComponentUILayout {
	private boolean _Saved = false;
	private Window _Window;

	public ComponentUIWindow(Ngeen ng, Entity ent) {
		super(ng, ent);
		WindowStyle style = new WindowStyle();
		BitmapFont font = (BitmapFont) _Ng.Loader.getAsset("LoadScene/fonts/impact.fnt").getData();
		style.titleFont = font;
		_Window = new Window("Text", style);
		getOwner().addSuperComponent((ComponentUILayout) this);
	}

	@Override
	public ComponentUIWindow remove() {
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
		return _Window;
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
