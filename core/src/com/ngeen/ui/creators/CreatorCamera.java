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
import com.ngeen.components.ButtonComponent;
import com.ngeen.components.CameraComponent;
import com.ngeen.ui.Interface;

public class CreatorCamera {

	public static ImageButton cameraNew, cameraDel;
	public static Label cameraTitle, cameraHeightLabel, cameraWidthLabel;
	public static TextField cameraHeight, cameraWidth;

	public static void createCamera() {
		final TextureAtlas buttons = Creator.buttons;
		final Skin skin = Creator.skin;
		Creator.camera = new Table(skin);
		final Table camera = Creator.camera;
		camera.setBackground("square");

		cameraNew = new ImageButton(new TextureRegionDrawable(
				buttons.findRegion("new", 0)), new TextureRegionDrawable(
				buttons.findRegion("new", 1)));
		cameraDel = new ImageButton(new TextureRegionDrawable(
				buttons.findRegion("exit", 0)), new TextureRegionDrawable(
				buttons.findRegion("exit", 1)));
		Table holder = new Table(skin);
		holder.add(cameraNew);
		holder.add(cameraDel);

		cameraWidthLabel = new Label("Width: ", skin);
		cameraHeightLabel = new Label("Height: ", skin);
		cameraWidth = new TextField("", skin);
		cameraHeight = new TextField("", skin);

		cameraTitle = new Label("Camera: ", skin);

		camera.add(cameraTitle).expandX().fillX();
		camera.add(holder);
		camera.row();
		camera.add(cameraWidthLabel);
		camera.add(cameraWidth);
		camera.row();
		camera.add(cameraHeightLabel);
		camera.add(cameraHeight);

		// add
		cameraNew.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				addCamera();
				Interface.ng.entityHelper.addComponent(CameraComponent.class, Interface.ng.getById(Interface.selected));
			};
		});

		// remove
		cameraDel.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				removeCamera();
				Interface.ng.entityHelper.removeComponent(CameraComponent.class, Interface.ng.getById(Interface.selected));
			};
		});
	}

	public static void addCamera() {
		createCamera();
		Interface.components.getCells().get(12).setActor(Creator.camera);
	}

	public static void removeCamera() {
		cameraHeightLabel.remove();
		cameraWidthLabel.remove();
		cameraHeight.remove();
		cameraWidth.remove();
		cameraDel.remove();

	}
}
