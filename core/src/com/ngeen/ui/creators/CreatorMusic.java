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
import com.ngeen.components.CameraComponent;
import com.ngeen.components.MusicComponent;
import com.ngeen.ui.Interface;

public class CreatorMusic {

	public static ImageButton musicNew, musicDel;
	public static Label musicTitle, musicResourceLabel;
	public static TextField musicResource;

	public static void createMusic() {
		final TextureAtlas buttons = Creator.buttons;
		final Skin skin = Creator.skin;
		Creator.music = new Table(skin);
		Table music = Creator.music;
		music.setBackground("square");

		musicNew = new ImageButton(new TextureRegionDrawable(
				buttons.findRegion("new", 0)), new TextureRegionDrawable(
				buttons.findRegion("new", 1)));
		musicDel = new ImageButton(new TextureRegionDrawable(
				buttons.findRegion("exit", 0)), new TextureRegionDrawable(
				buttons.findRegion("exit", 1)));
		Table holder = new Table(skin);
		holder.add(musicNew);
		holder.add(musicDel);

		// add
		musicNew.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				addMusic();
				Interface.ng.entityHelper.addComponent(MusicComponent.class, Interface.ng.getById(Interface.selected));
			};
		});

		// remove
		musicDel.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				removeMusic();
				Interface.ng.entityHelper.removeComponent(MusicComponent.class, Interface.ng.getById(Interface.selected));
			};
		});

		musicTitle = new Label("Music: ", skin);
		musicResourceLabel = new Label("Resource: ", skin);
		musicResource = new TextField("", skin);
		music.add(musicTitle).expandX().fillX();
		music.add(holder);
		music.row();
		music.add(musicResourceLabel);
		music.add(musicResource);
		music.row();
	}

	public static void addMusic() {
		createMusic();
		Interface.components.getCells().get(10).setActor(Creator.music);
	}

	public static void removeMusic() {
		musicDel.remove();
		musicResourceLabel.remove();
		musicResource.remove();
	}
}
