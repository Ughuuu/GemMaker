package com.ngeen.component.ui;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.ngeen.component.ComponentBase;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public abstract class ComponentUILayout extends ComponentBase{
	protected WidgetGroup _Layout;

	protected boolean _TopAlign, _BottomAlign, _RightAlign, _LeftAlign, _ExpandX, _ExpandY, _FillX, _FillY, _Uniform;
	protected Value _Width, _Height, _PadLeft, _PadRight, _PadTop, _PadBottom, _SpaceLeft, _SpaceRight, _SpaceTop, _SpaceBottom;
	protected int _ColSpan=1;
	
	public ComponentUILayout(Ngeen ng, Entity ent) {
		super(ng, ent);
	}
	
	public void act(float act) {
		_Layout.act(act);
	}
	
	protected <T extends ComponentUILayout> void add(T comp){
		
	}

	protected <T extends ComponentUIWidget> void add(T comp){
		
	}
	
	@Override
	protected void Save(XmlWriter element) throws Exception {
		//Don't save it, regenerate it.
	}

	@Override
	protected void Load(Element element) throws Exception {
		//Don't load it, regenerate it.
	}
}
