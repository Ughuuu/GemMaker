package com.ngeen.component;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentRigid extends ComponentBase {

	public ComponentRigid(Ngeen ng, Entity ent) {
		super(ng, ent);
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component")
		.attribute("_Type", _Type.getName())
		       .pop();
	}

	@Override
	protected void Load(Element element) throws Exception {
	}
}
