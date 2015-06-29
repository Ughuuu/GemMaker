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

public class CreatorResource {

	public static ImageButton resourceNew, resourceDel;
	public static Label resourceTitle, resourceTypeLabel;
	public static TextField resourceType;

	public static void createResource() {

		final TextureAtlas buttons = Creator.buttons;
		final Skin skin = Creator.skin;
		Creator.resource = new Table(skin);
		Table resource = Creator.resource;
		resource.setBackground("square");

		resourceNew = new ImageButton(new TextureRegionDrawable(
				buttons.findRegion("new", 0)), new TextureRegionDrawable(
				buttons.findRegion("new", 1)));
		resourceDel = new ImageButton(new TextureRegionDrawable(
				buttons.findRegion("exit", 0)), new TextureRegionDrawable(
				buttons.findRegion("exit", 1)));
		Table holder = new Table(skin);
		holder.add(resourceNew);
		holder.add(resourceDel);

		resourceTitle = new Label("Resource: ", skin);
		resourceTypeLabel = new Label("Type: ", skin);
		resourceType = new TextField("", skin);
		resource.add(resourceTitle).colspan(2).expandX().fillX();
		resource.row();
		resource.add(resourceTypeLabel);
		resource.add(resourceType);
		resource.row();
	}

	public static void addResource() {
		createResource();
		Interface.components.getCells().get(13).setActor(Creator.resource);
	}

	public static void removeResource() {
		resourceTypeLabel.remove();
		resourceType.remove();
		resourceDel.remove();
		resourceTitle.remove();
	}
}
