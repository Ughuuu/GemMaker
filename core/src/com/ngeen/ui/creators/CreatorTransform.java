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

public class CreatorTransform {

	public static ImageButton transformNew, transformDel;
	public static Label transformTitle, transformPositionXLabel,
			transformPositionYLabel, transformAngleLabel, transformScaleXLabel,
			transformScaleYLabel, transformDepthLabel;
	public static TextField transformPositionX, transformPositionY,
			transformAngle, transformScaleX, transformScaleY, transformDepth;

	public static void removeTransform() {
		transformAngle.remove();
		transformAngleLabel.remove();
		transformDepth.remove();
		transformDepthLabel.remove();
		transformPositionX.remove();
		transformPositionXLabel.remove();
		transformPositionY.remove();
		transformPositionYLabel.remove();		
		transformScaleX.remove();
		transformScaleXLabel.remove();
		transformScaleY.remove();
		transformScaleYLabel.remove();
		transformDel.remove();
	}

	public static void addTransform() {
		createTransform();
		Interface.components.getCells().get(2).setActor(Creator.transform);
	}

	public static void createTransform() {
		final TextureAtlas buttons = Creator.buttons;
		final Skin skin = Creator.skin;
		Creator.transform = new Table(skin);
		Table transform = Creator.transform;
		transform.setBackground("square");

		transformTitle = new Label("Transform: ", skin);
		transformPositionXLabel = new Label("Position X: ", skin);
		transformPositionX = new TextField("", skin);
		transformPositionYLabel = new Label("Position Y: ", skin);
		transformPositionY = new TextField("", skin);
		transformAngleLabel = new Label("Angle: ", skin);
		transformAngle = new TextField("", skin);
		transformScaleXLabel = new Label("Scale X: ", skin);
		transformScaleX = new TextField("", skin);
		transformScaleYLabel = new Label("Scale Y: ", skin);
		transformScaleY = new TextField("", skin);
		transformDepthLabel = new Label("Depth: ", skin);
		transformDepth = new TextField("", skin);

		transformNew = new ImageButton(new TextureRegionDrawable(
				buttons.findRegion("new", 0)), new TextureRegionDrawable(
				buttons.findRegion("new", 1)));
		transformDel = new ImageButton(new TextureRegionDrawable(
				buttons.findRegion("exit", 0)), new TextureRegionDrawable(
				buttons.findRegion("exit", 1)));
		Table holder = new Table(skin);
		holder.add(transformNew);
		holder.add(transformDel);

		transform.add(transformTitle).expandX().fillX();
		transform.add(holder);
		transform.row();
		transform.add(transformPositionXLabel);
		transform.add(transformPositionX);
		transform.row();
		transform.add(transformPositionYLabel);
		transform.add(transformPositionY);
		transform.row();
		transform.add(transformAngleLabel);
		transform.add(transformAngle);
		transform.row();
		transform.add(transformScaleXLabel);
		transform.add(transformScaleX);
		transform.row();
		transform.add(transformScaleYLabel);
		transform.add(transformScaleY);
		transform.row();
		transform.add(transformDepthLabel);
		transform.add(transformDepth);
		transform.row();

		transform.setBackground("square");

		// add
		transformNew.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				addTransform();
			};
		});

		// remove
		transformDel.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				removeTransform();
			};
		});
	}
}
