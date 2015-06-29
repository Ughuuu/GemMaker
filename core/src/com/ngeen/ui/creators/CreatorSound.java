package com.ngeen.ui.creators;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.ngeen.components.ScriptComponent;
import com.ngeen.components.SoundComponent;
import com.ngeen.ui.Interface;

public class CreatorSound {

	public static ImageButton soundNew, soundDel;
	public static Label soundTitle, soundResourceLabel;
	public static TextField soundResource;


	public static void createSound() {

		final TextureAtlas buttons = Creator.buttons;
		final Skin skin = Creator.skin;
		Creator.sound = new Table(skin);
		Table sound = Creator.sound;
		sound.setBackground("square");
		
		soundNew = new ImageButton(new TextureRegionDrawable(
				buttons.findRegion("new", 0)), new TextureRegionDrawable(
				buttons.findRegion("new", 1)));
		soundDel = new ImageButton(new TextureRegionDrawable(
				buttons.findRegion("exit", 0)), new TextureRegionDrawable(
				buttons.findRegion("exit", 1)));
		Table holder = new Table(skin);
		holder.add(soundNew);
		holder.add(soundDel);

		// add
		soundNew.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				addSound();
				Interface.ng.entityHelper.addComponent(SoundComponent.class, Interface.ng.getById(Interface.selected));
			};
		});

		// remove
		soundDel.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				removeSound();
				Interface.ng.entityHelper.removeComponent(SoundComponent.class, Interface.ng.getById(Interface.selected));
			};
		});
		

		soundTitle = new Label("Sound: ", skin);
		soundResourceLabel = new Label("Resource: ", skin);
		soundResource = new TextField("", skin);
		sound.add(soundTitle).expandX().fillX();
		sound.add(holder);
		sound.row();
		sound.add(soundResourceLabel);
		sound.add(soundResource);
		sound.row();
	}
	
	public static void addSound() {
		createSound();
		Interface.components.getCells().get(9).setActor(Creator.sound);
	}

	public static void removeSound() {
		soundResourceLabel.remove();
		soundResource.remove();
		soundDel.remove();
	}
}
