package com.ngeen.ui.creators;

import com.artemis.Entity;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.ngeen.components.TagComponent;
import com.ngeen.components.TextComponent;
import com.ngeen.ui.Interface;

public class CreatorTag {

	public static ImageButton tagDel;
	public static Label tagTitle, tagNameLabel;
	public static TextField tagName;

	public static void setListeners(){
		tagName.setTextFieldListener(new TextFieldListener() {
			
			@Override
			public void keyTyped(TextField textField, char c) {
				Entity ent = Interface.ng.getById(Interface.selected);
				if(ent == null)
					return;
				TagComponent tag = ent.getComponent(TagComponent.class);
				if(tag == null)
					return;
				//tag.
			}
		});
	}
	
	public static void createTag() {

		final TextureAtlas buttons = Creator.buttons;
		final Skin skin = Creator.skin;
		Creator.tag = new Table(skin);
		Table tag = Creator.tag;
		tag.setBackground("square");

		tagDel = new ImageButton(new TextureRegionDrawable(buttons.findRegion(
				"exit", 0)), new TextureRegionDrawable(buttons.findRegion(
				"exit", 1)));

		tagTitle = new Label("Tag: ", skin);
		tagNameLabel = new Label("Name: ", skin);
		tagName = new TextField("", skin);
		tag.add(tagTitle).expandX().fillX();
		tag.add(tagDel);
		tag.row();
		tag.add(tagNameLabel);
		tag.add(tagName);
		tag.row();
		
		tagName.setTextFieldListener(new TextFieldListener() {			
			@Override
			public void keyTyped(TextField textField, char c) {
				Interface.ng.getById(Interface.selected).getComponent(TagComponent.class).name = tagName.getText();
				Interface.reselect();
			};
		});

		// remove
		tagDel.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Interface.ng.removeEntity(Interface.selected);
				//Interface.deselect();
			};
		});
	}

	public static void removeTag() {
		tagName.setText("");
		tagDel.remove();
	}

	public static void addTag() {
		createTag();
		Interface.components.getCells().get(0).setActor(Creator.tag);
	}
}
