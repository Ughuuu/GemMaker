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

public class CreatorAnimation {

	public static ImageButton animationNew, animationDel;
	public static Label animationTitle, animationPageLabel,
			animationIndexLabel, animationResourceLabel;
	public static TextField animationPage, animationIndex, animationResource;

	public static void createAnimation() {
		final TextureAtlas buttons = Creator.buttons;
		final Skin skin = Creator.skin;
		Creator.animation = new Table(skin);
		Table animation = Creator.animation;
		animationNew = new ImageButton(new TextureRegionDrawable(
				buttons.findRegion("new", 0)), new TextureRegionDrawable(
				buttons.findRegion("new", 1)));
		animationDel = new ImageButton(new TextureRegionDrawable(
				buttons.findRegion("exit", 0)), new TextureRegionDrawable(
				buttons.findRegion("exit", 1)));
		Table holder = new Table(skin);
		holder.add(animationNew);
		holder.add(animationDel);

		animationTitle = new Label("Animation: ", skin);
		animationPageLabel = new Label("Page: ", skin);
		animationIndexLabel = new Label("Index: ", skin);
		animationResourceLabel = new Label("Resource: ", skin);
		animationPage = new TextField("", skin);
		animationIndex = new TextField("", skin);
		animationResource = new TextField("", skin);

		animation.add(animationTitle).expandX().fillX();
		animation.add(holder);
		animation.row();
		animation.add(animationPageLabel);
		animation.add(animationPage);
		animation.row();
		animation.add(animationIndexLabel);
		animation.add(animationIndex);
		animation.row();
		animation.add(animationResourceLabel);
		animation.add(animationResource);
		animation.row();

		// add
		animationNew.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				addAnimation();
			};
		});

		// remove
		animationDel.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				removeAnimation();
			};
		});
		animation.setBackground("square");
	}

	public static void removeAnimation() {
		animationPage.remove();
		animationResource.remove();
		animationIndex.remove();
		animationPageLabel.remove();
		animationResourceLabel.remove();
		animationIndexLabel.remove();
		animationDel.remove();
	}

	public static void addAnimation() {
		createAnimation();
		Interface.components.getCells().get(6).setActor(Creator.animation);
	}

}
