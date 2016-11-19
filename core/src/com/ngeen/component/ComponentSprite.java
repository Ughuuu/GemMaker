package com.ngeen.component;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.asset.Asset;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.ComponentSpokesman;
import com.ngeen.entity.Entity;

public class ComponentSprite extends ComponentBase {
    private String _TextureAsset;
    private Sprite spr;

    public ComponentSprite(Ngeen ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
        super(ng, ent, factory, _ComponentSpokesman);
    }

    public Color getColor() {
        return spr.getColor();
    }

    public void setColor(Color col) {
        spr.setColor(col);
    }

    public Sprite getSprite() {
        return spr;
    }

    public Texture getTexture(Asset<Texture> tex) {
        return spr.getTexture();
    }

    @Override
    public void notifyWithComponent(ComponentPoint point) {
        spr.setOrigin(spr.getWidth()/2, spr.getHeight()/2);
        spr.setPosition(point.getPosition().x - spr.getWidth() / 2, point.getPosition().y - spr.getHeight() / 2);
        spr.setScale(point.getScale().x, point.getScale().y);
        spr.setRotation(point.getRotation().z);
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
    protected ComponentBase Load(Element element) throws Exception {
        spr = new Sprite();
        _TextureAsset = element.getChildByName("Sprite").get("TextureAtlas");
        setTexture(_TextureAsset);
        return this;
    }

    @Override
    protected void Save(XmlWriter element) throws Exception {
        element.element("Component").attribute("Type", _Type.getName()).element("Sprite")
                .attribute("TextureAtlas", _TextureAsset).pop().pop();
    }

    @Override
    protected void visitComponent(ComponentBase component, ComponentFactory factory) {
        component.notifyWithComponent(this);
    }
}
