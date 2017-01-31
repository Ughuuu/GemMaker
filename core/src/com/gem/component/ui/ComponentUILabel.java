package com.gem.component.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.gem.component.ComponentBase;
import com.gem.component.ComponentFactory;
import com.gem.component.ComponentPoint;
import com.gem.engine.Gem;
import com.gem.entity.ComponentSpokesman;
import com.gem.entity.Entity;

public class ComponentUILabel extends ComponentUIWidget {
    private String assetBackground = null;
    private String assetFont = "engine/fonts/impact.fnt";
    private Label label;
    private boolean saved = false;

    public ComponentUILabel(Gem ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
        super(ng, ent, factory, _ComponentSpokesman);
        LabelStyle style = new LabelStyle();
        // TODO make asset background equal to something
        if (assetBackground != null && gem.loader.getAsset(assetBackground) != null) {
            style.background = new TextureRegionDrawable(
                    new TextureRegion(((Texture) gem.loader.getAsset(assetBackground).getAsset())));
        }
        style.font = (BitmapFont) gem.loader.getAsset(assetFont).getAsset();
        style.font.getData().scale(0.1f);
        label = new Label("Text", style);
        label.setWrap(false);
        label.pack();
    }

    public String getFont() {
        return assetFont;
    }

    public void setFont(String fontName) {
        assetFont = fontName;
        LabelStyle style = label.getStyle();
        style.font = (BitmapFont) gem.loader.getAsset(assetFont).getAsset();
        label.setStyle(style);
    }

    public String getText() {
        return label.getText().toString();
    }

    public void setText(String text) {
        label.setText(text);
    }

    @Override
    public ComponentUILabel remove() {
        getOwner().removeComponent(ComponentUIWidget.class);
        owner.removeComponent(this.getClass(), id);
        return this;
    }

    @Override
    public void notifyWithComponent(ComponentPoint point) {
        Actor actor = getActor();
        //actor.setOrigin(Align.center);
        actor.setPosition(point.getPosition().x, point.getPosition().y);
        actor.setZIndex(_Depth);
        if (point.getScale().isZero() && point.getRotation().isZero()) {
            label.setFontScale(1, 1);
        } else {
            label.setFontScale(point.getScale().x * point.getScale().z, point.getScale().y * point.getScale().z);
            actor.setRotation(point.getRotation().z);
        }
        label.pack();
    }
    
    @Override
    public float getWidth(){
    	label.pack();
    	return label.getWidth();
    }
    
    @Override
    public float getHeight(){
    	label.pack();
    	return label.getHeight();
    }
    
    @Override
    public ComponentUILabel setWidth(float width){
    	label.pack();
    	label.setWidth(width);
    	label.pack();
    	return this;
    }
    
    @Override
    public ComponentUILabel setHeight(float height){
    	label.pack();
    	label.setHeight(height);
    	label.pack();
    	return this;
    }

    @Override
    protected Actor getActor() {
        return label;
    }
    
    @Override
    protected ComponentBase Load(Element element) throws Exception {
        try {
            setFont(element.getChildByName("Label").get("FontName"));
        } catch (Exception e) {
            setFont(assetFont);
        }
        try {
            setText(element.getChildByName("Label").get("Text"));
        } catch (Exception e) {
            setText("");
        }
        try {
        	float height = Float.parseFloat(element.getChildByName("Label").get("Height"));
            setHeight(height);
        } catch (Exception e) {
        }
        try {
        	float width = Float.parseFloat(element.getChildByName("Label").get("Width"));
            setWidth(width);
        } catch (Exception e) {
        }
        label.pack();
        return this;
    }

    @Override
    protected void Save(XmlWriter element) throws Exception {
        element.element("Component").attribute("Type", this.getClass().getName()).element("Label")
                .attribute("FontName", assetFont)
                .attribute("Text", getText())
                .attribute("Width", getWidth())
                .attribute("Height", getHeight())
                .pop().pop();
    }

    @Override
    protected void visitComponent(ComponentBase component, ComponentFactory factory) {
        factory.callComponentNotify(this, component);
    }
}
