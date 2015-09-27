package com.ngeen.ui.creators;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.ngeen.components.GroupComponent;
import com.ngeen.components.TagComponent;
import com.ngeen.ui.Interface;

public class CreatorGroup {

	public static Label groupTitle, groupNameLabel;
	public static TextField groupName;

	public static void createGroup() {
		final TextureAtlas buttons = Creator.buttons;
		final Skin skin = Creator.skin;
		Creator.group = new Table(skin);
		Table group = Creator.group;
		group.setBackground("square");

		Table holder = new Table(skin);

		groupTitle = new Label("Group: ", skin);
		groupNameLabel = new Label("Name: ", skin);
		groupName = new TextField("", skin);
		group.add(groupTitle).colspan(2).expandX().fillX();
		group.row();
		group.add(groupNameLabel);
		group.add(groupName);
		group.row();
		

		
		groupName.setTextFieldListener(new TextFieldListener() {			
			@Override
			public void keyTyped(TextField textField, char c) {
				Interface.ng.getById(Interface.selected).getComponent(GroupComponent.class).name = groupName.getText();
				Interface.reselect();
			};
		});

	}

	public static void removeGroup() {
		groupName.setText("");
	}

	public static void addGroup() {
		createGroup();
		Interface.components.getCells().get(1).setActor(Creator.group);
	}
}
