package com.gem.component.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.Align;
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
	}

	public String getFont() {
		return assetFont;
	}

	public String getText() {
		return label.getText().toString();
	}

	@Override
	public ComponentUILabel remove() {
		getOwner().removeComponent(ComponentUIWidget.class);
		Owner.removeComponent(this.getClass(), Id);
		return this;
	}

	public void setFont(String fontName) {
		assetFont = fontName;
		label.getStyle().font = (BitmapFont) gem.loader.getAsset(assetFont).getAsset();
	}

	public void setText(String text) {
		label.setText(text);
	}	

	@Override
	public void notifyWithComponent(ComponentPoint point) {
		Actor actor = getActor();
		actor.setOrigin(Align.center);
		actor.setPosition(point.getPosition().x, point.getPosition().y);
		actor.setZIndex(_Depth);
		if (point.getScale().isZero() && point.getRotation().isZero()) {
			label.setFontScale(1, 1);
		} else {
			label.setFontScale(point.getScale().x, point.getScale().y);
			actor.setRotation(point.getRotation().z);
		}
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
		return this;
	}

	@Override
	protected void Save(XmlWriter element) throws Exception {
		element.element("Component").attribute("Type", this.getClass().getName()).element("Label")
				.attribute("FontName", assetFont).attribute("Text", getText()).pop().pop();
	}

	@Override
	protected void visitComponent(ComponentBase component, ComponentFactory factory) {
		factory.callComponentNotify(this, component);
	}
}
