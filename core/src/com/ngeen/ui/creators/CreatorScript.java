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

public class CreatorScript {

	public static ImageButton scriptNew, scriptDel;
	public static Label scriptTitle, scriptScriptLabel, scriptEnableLabel;
	public static TextField scriptScript;
	public static CheckBox scriptEnable;

	public static void createScript() {

		final TextureAtlas buttons = Creator.buttons;
		final Skin skin = Creator.skin;
		Creator.script = new Table(skin);
		Table script = Creator.script;
		script.setBackground("square");

		scriptNew = new ImageButton(new TextureRegionDrawable(
				buttons.findRegion("new", 0)), new TextureRegionDrawable(
				buttons.findRegion("new", 1)));
		scriptDel = new ImageButton(new TextureRegionDrawable(
				buttons.findRegion("exit", 0)), new TextureRegionDrawable(
				buttons.findRegion("exit", 1)));
		Table holder = new Table(skin);
		holder.add(scriptNew);
		holder.add(scriptDel);

		// add
		scriptNew.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				addScript();
			};
		});

		// remove
		scriptDel.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				removeScript();
			};
		});

		scriptTitle = new Label("Script: ", skin);
		scriptScriptLabel = new Label("Class: ", skin);
		scriptScript = new TextField("", skin);
		scriptEnableLabel = new Label("Enable?: ", skin);
		scriptEnable = new CheckBox("", skin);
		script.add(scriptTitle).expandX().fillX();
		script.add(holder);
		script.row();
		script.add(scriptEnableLabel);
		script.add(scriptEnable);
		script.row();
		script.add(scriptScriptLabel);
		script.add(scriptScript);
		script.row();
	}

	public static void addScript() {
		createScript();
		Interface.components.getCells().get(11).setActor(Creator.script);
	}

	public static void removeScript() {
		scriptScriptLabel.remove();
		scriptScript.remove();
		scriptDel.remove();
		scriptEnableLabel.remove();
		scriptEnable.remove();
	}
}
