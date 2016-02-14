package com.ngeen.component.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentUIProgressBar extends ComponentUIWidget {
	private boolean _Saved = false;
	private ProgressBar _ProgressBar;
	
	public ComponentUIProgressBar(Ngeen ng, Entity ent) {
		super(ng, ent);
		ProgressBarStyle style = new ProgressBarStyle();
		_ProgressBar = new ProgressBar(0, 1, 0.1f, true, style);
		getOwner().addSuperComponent((ComponentUIWidget) this);
	}

	@Override
	public ComponentUIProgressBar remove() {
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
		return _ProgressBar;
	}
}
