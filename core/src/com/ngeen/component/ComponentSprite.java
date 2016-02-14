package com.ngeen.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.asset.Asset;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class ComponentSprite extends ComponentBase {
	private String _TextureAsset;
	private Sprite spr;

	public ComponentSprite(Ngeen ng, Entity ent) {
		super(ng, ent);
	}

	public Color getColor() {
		return spr.getColor();
	}

	public Sprite getSprite() {
		return spr;
	}

	public Texture getTexture(Asset<Texture> tex) {
		return spr.getTexture();
	}

	public void setColor(Color col) {
		spr.setColor(col);
	}

	public ComponentSprite setTexture(Asset<Texture> tex) {
		_TextureAsset = tex.getFolder() + tex.getPath();
		spr = new Sprite(tex.getData());
		return this;
	}

	public ComponentSprite setTexture(String tex) {
		_TextureAsset = tex;
		spr = new Sprite((Texture) _Ng.Loader.getAsset(tex).getData());
		return this;
	}

	@Override
	protected void Load(Element element) throws Exception {
		spr = new Sprite();
		_TextureAsset = element.getChildByName("_TextureAsset").get("String");
		setTexture(_TextureAsset);
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component").attribute("_Type", _Type.getName()).element("_TextureAsset")
				.attribute("String", _TextureAsset).pop().pop();
	}
}
