package com.gem.component.ui;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.gem.component.ComponentBase;
import com.gem.component.ComponentFactory;
import com.gem.engine.Gem;
import com.gem.entity.ComponentSpokesman;
import com.gem.entity.Entity;

public class ComponentUIWindow extends ComponentUILayout {
	private boolean _Saved = false;
	private Window _Window;

	public ComponentUIWindow(Gem ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
		super(ng, ent, factory, _ComponentSpokesman);
		WindowStyle style = new WindowStyle();
		BitmapFont font = (BitmapFont) gem.loader.getAsset("LoadScene/fonts/impact.fnt").getAsset();
		style.titleFont = font;
		_Window = new Window("Text", style);
	}

	@Override
	public ComponentUIWindow remove() {
		getOwner().removeComponent(ComponentUIWidget.class);
		Owner.removeComponent(this.getClass(), Id);
		return this;
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
	protected WidgetGroup get() {
		return _Window;
	}

	@Override
	protected ComponentBase Load(Element element) throws Exception {
		return this;
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component").attribute("Type", this.getClass().getName()).pop();
	}

	@Override
	protected void swap(ComponentUIBase a, ComponentUIBase b) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void visitComponent(ComponentBase component, ComponentFactory factory) {
		factory.callComponentNotify(this, component);
	}
}
