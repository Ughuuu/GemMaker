package com.ngeen.component.ui;

import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.component.ComponentBase;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentUIButtonGroup extends ComponentBase {
	private ButtonGroup _ButtonGroup;

	public ComponentUIButtonGroup(Ngeen ng, Entity ent) {
		super(ng, ent);
		_ButtonGroup = new ButtonGroup();
	}

	@Override
	protected void Load(Element element) throws Exception {
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component").attribute("_Type", _Type.getName()).pop();
	}
}
