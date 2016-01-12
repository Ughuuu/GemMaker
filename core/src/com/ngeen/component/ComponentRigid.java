package com.ngeen.component;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.engine.Ngeen;

public class ComponentRigid extends ComponentBase {

	public ComponentRigid(Ngeen ng) {
		super(ng);
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component")
		.attribute("_Type", "ComponentRigid")
		       .pop();
	}

	@Override
	protected void Load(Element element) throws Exception {
	}
}
