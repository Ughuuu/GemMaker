package com.gem.component.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.gem.component.ComponentBase;
import com.gem.component.ComponentFactory;
import com.gem.engine.Gem;
import com.gem.entity.ComponentSpokesman;
import com.gem.entity.Entity;

public abstract class ComponentUILayout extends ComponentUIBase {
	protected int _ColSpan = 1;

	protected boolean _ExpandX, _ExpandY, _FillX, _FillY, _Uniform;
	protected float _Width, _Height, _PadLeft, _PadRight, _PadTop, _PadBottom, _SpaceLeft, _SpaceRight, _SpaceTop,
			_SpaceBottom;

	public ComponentUILayout(Gem ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
		super(ng, ent, factory, _ComponentSpokesman);
	}

	public void act(float act) {
		get().act(act);
	}

	protected abstract WidgetGroup get();

	@Override
	protected Actor getActor() {
		return get();
	}

	@Override
	protected ComponentBase Load(Element element) throws Exception {
		return this;
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		// Don't save it, regenerate it.
	}
}
