package com.gem.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.gem.asset.Asset;
import com.gem.engine.Gem;
import com.gem.entity.ComponentSpokesman;
import com.gem.entity.Entity;

import lombok.val;

public class ComponentSprite extends ComponentBase {
	private String _TextureAsset;
	private Sprite spr;

	public ComponentSprite(Gem ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
		super(ng, ent, factory, _ComponentSpokesman);
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

	@Override
	public void notifyWithComponent(ComponentPoint point) {
		spr.setOrigin(spr.getWidth() / 2, spr.getHeight() / 2);
		spr.setPosition(point.getPosition().x - spr.getWidth() / 2, point.getPosition().y - spr.getHeight() / 2);
		spr.setScale(point.getScale().x, point.getScale().y);
		spr.setRotation(point.getRotation().z);
	}
	
	public void setWidth(int width){
		spr.setSize(width, spr.getHeight());
	}
	
	public void setHeight(int height){
		spr.setSize(spr.getWidth(), height);
	}

	public void setColor(Color col) {
		spr.setColor(col);
	}

	public ComponentSprite setTexture(Asset<Texture> tex) {
		_TextureAsset = tex.getFolder() + tex.getPath();
		Texture texture = (Texture)tex.getAsset();
		spr.setTexture(texture);
		spr.setSize(texture.getWidth(), texture.getHeight());
		spr.setRegion(0, 0, texture.getWidth(), texture.getHeight());
		spr.setColor(1, 1, 1, 1);
		spr.setOrigin(texture.getWidth(), texture.getHeight());
		return this;
	}

	public ComponentSprite setTexture(String tex) {
		_TextureAsset = tex;
		setTexture(gem.loader.getAsset(tex));
		return this;
	}

	@Override
	protected ComponentBase Load(Element element) throws Exception {
		spr = new Sprite();
		_TextureAsset = element.getChildByName("Sprite").get("Texture");
		setTexture(_TextureAsset);
		try{
			spr.setColor(Color.valueOf(element.getChildByName("Sprite").get("Color")));
			float width = element.getChildByName("Sprite").getFloat("Width");
			float height = element.getChildByName("Sprite").getFloat("Height");
			spr.setSize(width, height);
		}catch(Exception e){
		}
		return this;
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component").attribute("Type", Type.getName())
			.element("Sprite")
				.attribute("Texture", _TextureAsset)
				.attribute("Color", spr.getColor())
				.attribute("Width", spr.getWidth())
				.attribute("Height", spr.getHeight())
			.pop()
		.pop();
	}

	@Override
	protected void visitComponent(ComponentBase component, ComponentFactory factory) {
		component.notifyWithComponent(this);
	}
}
