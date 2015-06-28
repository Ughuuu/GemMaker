package com.ngeen.ui.creators;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.ngeen.ui.Interface;

public class CreatorTag {

	public static Label tagTitle, tagNameLabel;
	public static TextField tagName;

	public static void createTag() {

		final TextureAtlas buttons = Creator.buttons;
		final Skin skin = Creator.skin;
		Creator.tag = new Table(skin);
		Table tag = Creator.tag;
		tag.setBackground("square");

		tagTitle = new Label("Tag: ", skin);
		tagNameLabel = new Label("Name: ", skin);
		tagName = new TextField("", skin);
		tag.add(tagTitle).colspan(2).expandX().fillX();
		tag.row();
		tag.add(tagNameLabel);
		tag.add(tagName);
		tag.row();
	}

	public static void removeTag() {
	}

	public static void addTag() {
		createTag();
		Interface.components.getCells().get(0).setActor(Creator.tag);
	}
}
