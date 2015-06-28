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
import com.ngeen.ui.Interface;

public class CreatorPhysics {

	public static ImageButton physicsNew, physicsDel;
	public static Label physicsTitle;
	
	public static void addPhysics() {
		createPhysics();
		Interface.components.getCells().get(4).setActor(Creator.physics);
	}

	public static void removePhysics() {
		physicsDel.remove();
	}

	public static void createPhysics() {
		final TextureAtlas buttons = Creator.buttons;
		final Skin skin = Creator.skin;
		Creator.physics = new Table(skin);
		Table physics = Creator.physics;
		physics.setBackground("square");
		
		physicsNew = new ImageButton(new TextureRegionDrawable(
				buttons.findRegion("new", 0)), new TextureRegionDrawable(
				buttons.findRegion("new", 1)));
		physicsDel = new ImageButton(new TextureRegionDrawable(
				buttons.findRegion("exit", 0)), new TextureRegionDrawable(
				buttons.findRegion("exit", 1)));
		Table holder = new Table(skin);
		holder.add(physicsNew);
		holder.add(physicsDel);

		// add
		physicsNew.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				addPhysics();
			};
		});

		// remove
		physicsDel.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				removePhysics();
			};
		});

		physicsTitle = new Label("Physics: ", skin);
		physics.add(physicsTitle).expandX().fillX();
		physics.add(holder);
		physics.row();
	}
}
