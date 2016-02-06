package com.ngeen.component.ui;

import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentUIStack extends ComponentUILayout {
	private boolean _Saved = false;
	
	public ComponentUIStack(Ngeen ng, Entity ent) {
		super(ng, ent);
		_Layout = new Stack();
		getOwner().addSuperComponent((ComponentUILayout)this);
	}

	@Override
	public void act(float delta) {
		_Layout.act(delta);
	}

	@Override
	public ComponentUIStack remove() {
		getOwner().removeComponent(ComponentUILayout.class);
		_Owner.removeComponent(this.getClass(), Id);
		return this;
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		if(_Saved){
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
