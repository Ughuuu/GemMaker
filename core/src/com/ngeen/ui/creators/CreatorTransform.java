package com.ngeen.ui.creators;

import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldListener;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent;
import com.ngeen.components.GroupComponent;
import com.ngeen.components.TextureComponent;
import com.ngeen.components.TransformComponent;
import com.ngeen.ui.Interface;

public class CreatorTransform {

	public static ImageButton transformNew, transformDel;
	public static Label transformTitle, transformPositionXLabel, transformPositionYLabel, transformAngleLabel,
			transformScaleXLabel, transformScaleYLabel, transformDepthLabel;
	public static TextField transformPositionX, transformPositionY, transformAngle, transformScaleX, transformScaleY,
			transformDepth;

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

		transformNew = new ImageButton(new TextureRegionDrawable(buttons.findRegion("new", 0)),
				new TextureRegionDrawable(buttons.findRegion("new", 1)));
		transformDel = new ImageButton(new TextureRegionDrawable(buttons.findRegion("exit", 0)),
				new TextureRegionDrawable(buttons.findRegion("exit", 1)));
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
				Interface.ng.entityHelper.addComponent(TransformComponent.class,
						Interface.ng.getById(Interface.selected));
			};
		});

		// remove
		transformDel.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				removeTransform();
				Interface.ng.entityHelper.removeComponent(TransformComponent.class,
						Interface.ng.getById(Interface.selected));
			};
		});
		
		transformPositionX.setTextFieldListener(new TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char c) {
				try {
					Interface.ng.getById(Interface.selected).getComponent(TransformComponent.class).position.x = Float
							.parseFloat(transformPositionX.getText());
				} catch (Exception e) {

				}
				Interface.reselect();
			};
		});

		transformPositionY.setTextFieldListener(new TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char c) {
				try {
					Interface.ng.getById(Interface.selected).getComponent(TransformComponent.class).position.y = Float
							.parseFloat(transformPositionY.getText());
				} catch (Exception e) {

				}
				Interface.reselect();
			};
		});

		transformAngle.setTextFieldListener(new TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char c) {
				try {
					Interface.ng.getById(Interface.selected).getComponent(TransformComponent.class).angle = Float
							.parseFloat(transformAngle.getText());
				} catch (Exception e) {

				}
				Interface.reselect();
			};
		});

		transformScaleX.setTextFieldListener(new TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char c) {
				try {
					Interface.ng.getById(Interface.selected).getComponent(TransformComponent.class).scale.x = Float
							.parseFloat(transformScaleX.getText());
				} catch (Exception e) {

				}
				Interface.reselect();
			};
		});

		transformScaleY.setTextFieldListener(new TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char c) {
				try {
					Interface.ng.getById(Interface.selected).getComponent(TransformComponent.class).scale.y = Float
							.parseFloat(transformScaleY.getText());
				} catch (Exception e) {

				}
				Interface.reselect();
			};
		});

		transformDepth.setTextFieldListener(new TextFieldListener() {
			@Override
			public void keyTyped(TextField textField, char c) {
				try {
					Interface.ng.getById(Interface.selected).getComponent(TransformComponent.class).z = Integer
							.parseInt(transformDepth.getText());
				} catch (Exception e) {

				}
				Interface.reselect();
			};
		});
	}
}
