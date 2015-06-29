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
import com.ngeen.components.MusicComponent;
import com.ngeen.components.ParticleComponent;
import com.ngeen.ui.Interface;

public class CreatorParticle {

	public static ImageButton particleNew, particleDel;
	public static Label partTitle, partDrawLabel, partResourceLabel;
	public static TextField partResource;
	public static CheckBox partDraw;

	public static void createParticle() {

		final TextureAtlas buttons = Creator.buttons;
		final Skin skin = Creator.skin;
		Creator.particle = new Table(skin);
		Table particle = Creator.particle;
		particle.setBackground("square");
		
		particleNew = new ImageButton(new TextureRegionDrawable(
				buttons.findRegion("new", 0)), new TextureRegionDrawable(
				buttons.findRegion("new", 1)));
		particleDel = new ImageButton(new TextureRegionDrawable(
				buttons.findRegion("exit", 0)), new TextureRegionDrawable(
				buttons.findRegion("exit", 1)));
		Table holder = new Table(skin);
		holder.add(particleNew);
		holder.add(particleDel);

		// add
		particleNew.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				addParticle();
				Interface.ng.entityHelper.addComponent(ParticleComponent.class, Interface.ng.getById(Interface.selected));
			};
		});

		// remove
		particleDel.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				removeParticle();
				Interface.ng.entityHelper.removeComponent(ParticleComponent.class, Interface.ng.getById(Interface.selected));
			};
		});

		partTitle = new Label("Particle: ", skin);
		partResourceLabel = new Label("Resource: ", skin);
		partResource = new TextField("", skin);
		partDrawLabel = new Label("Draw?: ", skin);
		
		partDraw = new CheckBox("", skin);
		
		particle.add(partTitle).expandX().fillX();
		particle.add(holder);
		particle.row();
		particle.add(partDrawLabel);
		particle.add(partDraw);
		particle.row();
		particle.add(partResourceLabel);
		particle.add(partResource);
		particle.row();
	}
	
	public static void addParticle() {
		createParticle();
		Interface.components.getCells().get(5).setActor(Creator.particle);
	}
	
	public static void removeParticle() {
		partDrawLabel.remove();
		partResourceLabel.remove();
		partResource.remove();
		partDraw.remove();
		particleDel.remove();
	}
}
