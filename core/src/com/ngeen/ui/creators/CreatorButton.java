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

public class CreatorButton {

	public static ImageButton buttonNew, buttonDel;
	public static Label buttonTitle, buttonStateLabel, buttonStatesLabel;
	public static TextField buttonState, buttonStates;

	public static void createButton() {
		final TextureAtlas buttons = Creator.buttons;
		final Skin skin = Creator.skin;
		Creator.button = new Table(skin);
		final Table button = Creator.button;
		button.setBackground("square");
		buttonStateLabel = new Label("State: ", skin);
		buttonStatesLabel = new Label("States: ", skin);
		buttonState = new TextField("", skin);
		buttonStates = new TextField("", skin);

		buttonNew = new ImageButton(new TextureRegionDrawable(
				buttons.findRegion("new", 0)), new TextureRegionDrawable(
				buttons.findRegion("new", 1)));
		buttonDel = new ImageButton(new TextureRegionDrawable(
				buttons.findRegion("exit", 0)), new TextureRegionDrawable(
				buttons.findRegion("exit", 1)));
		Table holder = new Table(skin);
		holder.add(buttonNew);
		holder.add(buttonDel);

		buttonTitle = new Label("Button: ", skin);

		button.add(buttonTitle).expandX().fillX();
		button.add(holder);
		button.row();
		button.add(buttonStatesLabel);
		button.add(buttonStates);
		button.row();
		button.add(buttonStateLabel);
		button.add(buttonState);
		button.row();

		// add
		buttonNew.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				addButton();
			};
		});

		// remove
		buttonDel.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				removeButton();
			};
		});
	}

	public static void addButton() {
		createButton();
		Interface.components.getCells().get(7).setActor(Creator.button);
	}

	public static void removeButton() {
		buttonState.remove();
		buttonStates.remove();
		buttonStatesLabel.remove();
		buttonStateLabel.remove();
		buttonDel.remove();
	}
}
