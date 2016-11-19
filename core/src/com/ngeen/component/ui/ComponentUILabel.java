package com.ngeen.component.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.component.ComponentBase;
import com.ngeen.component.ComponentFactory;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.ComponentSpokesman;
import com.ngeen.entity.Entity;

public class ComponentUILabel extends ComponentUIWidget {
    private String _AssetBackground = null;
    private String _AssetFont = "LoadScene/fonts/impact.fnt";
    private Label _Label;
    private boolean _Saved = false;

    public ComponentUILabel(Ngeen ng, Entity ent, ComponentFactory factory, ComponentSpokesman _ComponentSpokesman) {
        super(ng, ent, factory, _ComponentSpokesman);
        LabelStyle style = new LabelStyle();
        if (_Ng.Loader.getAsset(_AssetBackground) != null) {
            style.background = new TextureRegionDrawable(
                    new TextureRegion(((Texture) _Ng.Loader.getAsset(_AssetBackground).getData())));
        }
        style.font = (BitmapFont) _Ng.Loader.getAsset(_AssetFont).getData();
        _Label = new Label("Text", style);
    }

    public void setFont(String fontName){
        _AssetFont = fontName;
        _Label.getStyle().font = (BitmapFont) _Ng.Loader.getAsset(_AssetFont).getData();
    }

    public String getFont(){
        return _AssetFont;
    }

    public String getText() {
        return _Label.getText().toString();
    }

    public void setText(String text) {
        _Label.setText(text);
    }

    @Override
    public ComponentUILabel remove() {
        getOwner().removeComponent(ComponentUIWidget.class);
        _Owner.removeComponent(this.getClass(), Id);
        return this;
    }

    @Override
    protected Actor getActor() {
        return _Label;
    }

    @Override
    protected ComponentBase Load(Element element) throws Exception {
        setFont(element.getChildByName("Label").get("FontName"));
        setText(element.getChildByName("Label").get("Text"));
        return this;
    }

    @Override
    protected void Save(XmlWriter element) throws Exception {
        element.element("Component").attribute("Type", this.getClass().getName())
                .element("Label").attribute("FontName", _AssetFont).attribute("Text", getText()).pop().pop();
    }

    @Override
    protected void visitComponent(ComponentBase component, ComponentFactory factory) {
        factory.callComponentNotify(this, component);
    }
}
