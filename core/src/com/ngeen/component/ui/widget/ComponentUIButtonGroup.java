package com.ngeen.component.ui.widget;

import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.ngeen.component.ComponentBase;
import com.ngeen.component.ComponentUILayout;
import com.ngeen.component.ComponentUIWidget;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentUIButtonGroup extends ComponentBase{
	private ButtonGroup _ButtonGroup;
	
	public ComponentUIButtonGroup(Ngeen ng, Entity ent) {
		super(ng, ent);
		_ButtonGroup = new ButtonGroup();
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component").attribute("_Type", _Type.getName()).pop();
	}

	@Override
	protected void Load(Element element) throws Exception {
	}
}
