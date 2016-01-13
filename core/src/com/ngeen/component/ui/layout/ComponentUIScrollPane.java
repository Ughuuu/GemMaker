package com.ngeen.component.ui.layout;

import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.ngeen.component.ComponentUILayout;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentUIScrollPane extends ComponentUILayout {
	private boolean _Saved = false;
	
	public ComponentUIScrollPane(Ngeen ng, Entity ent) {
		super(ng, ent);
		//_Layout = new ScrollPane();
		getOwner().addSuperComponent((ComponentUILayout)this);
	}

	@Override
	public void act(float delta) {
		_Layout.act(delta);
	}

	@Override
	public ComponentUIScrollPane remove() {
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
