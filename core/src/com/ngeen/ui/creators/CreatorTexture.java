package com.ngeen.ui.creators;

import com.artemis.Entity;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
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
import com.ngeen.components.ResourceComponent;
import com.ngeen.components.TagComponent;
import com.ngeen.components.TextComponent;
import com.ngeen.components.TextureComponent;
import com.ngeen.ui.Interface;

public class CreatorTexture {

	public static ImageButton textureNew, textureDel;
	public static Label textureTitle, textureResourceLabel, textureDrawLabel;
	public static TextField textureResource;
	public static CheckBox textureDraw;

	public static void removeTexture() {
		textureResourceLabel.remove();
		textureResource.remove();
		textureDel.remove();
		textureDrawLabel.remove();
		textureDraw.remove();
	}

	public static void addTexture() {
		createTexture();
		Interface.components.getCells().get(3).setActor(Creator.texture);
	}

	public static void createTexture() {
		final TextureAtlas buttons = Creator.buttons;
		final Skin skin = Creator.skin;
		Creator.texture = new Table(skin);
		Table texture = Creator.texture;
		texture.setBackground("square");

		textureTitle = new Label("Texture: ", skin);
		textureResourceLabel = new Label("Resource: ", skin);
		textureDrawLabel = new Label("Draw?: ", skin);
		textureResource = new TextField("", skin);
		textureDraw = new CheckBox("", skin);

		
		textureNew = new ImageButton(new TextureRegionDrawable(
				buttons.findRegion("new", 0)), new TextureRegionDrawable(
				buttons.findRegion("new", 1)));
		textureDel = new ImageButton(new TextureRegionDrawable(
				buttons.findRegion("exit", 0)), new TextureRegionDrawable(
				buttons.findRegion("exit", 1)));
		Table holder = new Table(skin);
		holder.add(textureNew);
		holder.add(textureDel);

		// add
		textureNew.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				addTexture();
				Interface.ng.entityHelper.addComponent(TextureComponent.class, Interface.ng.getById(Interface.selected));
			};
		});

		// remove
		textureDel.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				removeTexture();
				Interface.ng.entityHelper.removeComponent(TextureComponent.class, Interface.ng.getById(Interface.selected));
			};
		});
		
		texture.add(textureTitle).expandX().fillX();
		texture.add(holder);
		texture.row();
		texture.add(textureDrawLabel);
		texture.add(textureDraw);
		texture.row();
		texture.add(textureResourceLabel);
		texture.add(textureResource);
		texture.row();
		
		textureDraw.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Interface.ng.getById(Interface.selected).getComponent(TextureComponent.class).draw = textureDraw.isChecked();
				Interface.reselect();
			};
		});
		
		textureResource.setTextFieldListener(new TextFieldListener() {			
			@Override
			public void keyTyped(TextField textField, char c) {
				Entity e = Interface.ng.getByTag(textureResource.getText());
				if(e == null)
					return;
				Interface.ng.getById(Interface.selected).getComponent(TextureComponent.class).resource_index = e.id;
				Interface.ng.getById(Interface.selected).getComponent(TextureComponent.class).tex = 
						new Sprite((Texture)e.getComponent(ResourceComponent.class).resource);
				Interface.reselect();
			};
		});
	}
}
