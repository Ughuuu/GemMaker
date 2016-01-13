package com.ngeen.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.ngeen.asset.Asset;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentSprite extends ComponentBase{
	private Sprite spr;
	private String _TextureAsset;
	
	public ComponentSprite(Ngeen ng, Entity ent) {
		super(ng, ent);
	}
	
	public ComponentSprite setTexture(Asset<Texture> tex){
		_TextureAsset = tex.getFolder() + tex.getPath();
		spr = new Sprite(tex.getData());
		return this;
	}
	
	public ComponentSprite setTexture(String tex){
		_TextureAsset = tex;
		spr = new Sprite((Texture) _Ng.Loader.getAsset(tex).getData());
		return this;
	}
	
	public Texture getTexture(Asset<Texture> tex){
		return spr.getTexture();
	}
	
	public Sprite getSprite(){
		return spr;
	}
	
	public void setColor(Color col){
		spr.setColor(col);
	}
	
	public Color getColor(){
		return spr.getColor();
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component")
				.attribute("_Type", this.getClass().getName())
				.attribute("_TextureAsset", _TextureAsset)
		       .pop();
	}

	@Override
	protected void Load(Element element) throws Exception {
		spr = new Sprite();
		_TextureAsset = element.get("_TextureAsset");
		setTexture(_TextureAsset);
	}
}
