package com.ngeen.ui.creators;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.ngeen.components.SoundComponent;
import com.ngeen.components.TextComponent;
import com.ngeen.ui.Interface;

public class CreatorText {

	public static ImageButton textNew, textDel;
	public static Label textTitle, textTextLabel, textDrawLabel, textResourceLabel;
	public static TextField textText, textResource;
	public static CheckBox textDraw;
	
	public static void createText() {
		final TextureAtlas buttons = Creator.buttons;
		final Skin skin = Creator.skin;
		Creator.text = new Table(skin);
		Table text = Creator.text;
		text.setBackground("square");

		textTitle = new Label("Text: ", skin);
		textTextLabel = new Label("String: ", skin);
		textText = new TextField("", skin);
		textResourceLabel = new Label("Resource: ", skin);
		textResource = new TextField("", skin);
		textDrawLabel = new Label("Draw?: ", skin);
		
		textDraw = new CheckBox("", skin);
		
		textNew = new ImageButton(new TextureRegionDrawable(buttons.findRegion(
				"new", 0)), new TextureRegionDrawable(buttons.findRegion("new",
				1)));
		textDel = new ImageButton(new TextureRegionDrawable(buttons.findRegion(
				"exit", 0)), new TextureRegionDrawable(buttons.findRegion(
				"exit", 1)));
		Table holder = new Table(skin);
		holder.add(textNew);
		holder.add(textDel);

		// add
		textNew.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				addText();
				Interface.ng.entityHelper.addComponent(TextComponent.class, Interface.ng.getById(Interface.selected));
			};
		});

		// remove
		textDel.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				removeText();
				Interface.ng.entityHelper.removeComponent(TextComponent.class,
						Interface.ng.getById(Interface.selected));
			};
		});

		text.add(textTitle).expandX().fillX();
		text.add(holder);
		text.row();
		text.add(textTextLabel);
		text.add(textText);
		text.row();
		text.add(textDrawLabel);
		text.add(textDraw);
		text.row();
		text.add(textResourceLabel);
		text.add(textResource);
		text.row();
	}

	public static void removeText() {
		textTextLabel.remove();
		textDrawLabel.remove();
		textResourceLabel.remove();
		textText.remove();
		textResource.remove();
		textDraw.remove();
		textDel.remove();
	}
	
	public static void addText() {
		createText();
		Interface.components.getCells().get(8).setActor(Creator.text);
	}
}
