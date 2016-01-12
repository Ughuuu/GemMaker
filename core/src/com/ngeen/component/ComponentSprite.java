package com.ngeen.component;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.ngeen.asset.Asset;
import com.ngeen.engine.Ngeen;

public class ComponentSprite extends ComponentBase{
	private Sprite spr;
	
	public ComponentSprite(Ngeen ng) {
		super(ng);
		spr = new Sprite();
	}
	
	public void setTexture(Asset<Texture> tex){
		spr = new Sprite(tex.getData());
	}
	
	public Sprite getSprite(){
		return spr;
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component")
				.attribute("_Type", "ComponentSprite")
		       .pop();
	}

	@Override
	protected void Load(Element element) throws Exception {
		spr = new Sprite();
	}
}
