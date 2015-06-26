package com.ngeen.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ngeen.holder.Constant;
import com.ngeen.holder.Ngeen;

public class Interface extends Stage {
	private Skin skin;
	private TextureAtlas buttons;
	private Table top;
	private int w, h;

	final Ngeen ng;
	
	public static void print(String line){	
		System.out.print(line);	
	}
	
	public static void println(String line){
		System.out.println(line);
	}
	
	public Interface(Ngeen ng) {
		super(new ScreenViewport());
		this.ng = ng;
		w = Gdx.graphics.getWidth();
		h = Gdx.graphics.getHeight();
		init();
	}

	void init() {
		skin = new Skin(Gdx.files.internal("atlas/darkui.json"));
		buttons = new TextureAtlas("atlas/def_but.atlas");

		top = createTopMenu();
		this.addActor(top);
		this.setDebugAll(true);
	}

	Table createTopMenu() {
		Table table = new Table(skin);
		final ImageButton[] but = new ImageButton[7];
		final CheckBox[] box = new CheckBox[3];
		final CheckBoxStyle[] boxStyle = new CheckBoxStyle[3];
		boxStyle[0] = new CheckBoxStyle(new TextureRegionDrawable(
				buttons.findRegion("pause", 0)), new TextureRegionDrawable(
				buttons.findRegion("pause", 1)), new BitmapFont(), new Color());
		boxStyle[1] = new CheckBoxStyle(new TextureRegionDrawable(
				buttons.findRegion("play", 0)), new TextureRegionDrawable(
				buttons.findRegion("play", 1)), new BitmapFont(), new Color());
		boxStyle[2] = new CheckBoxStyle(new TextureRegionDrawable(
				buttons.findRegion("mute", 0)), new TextureRegionDrawable(
				buttons.findRegion("mute", 1)), new BitmapFont(), new Color());
		box[0] = new CheckBox("", boxStyle[0]);
		box[1] = new CheckBox("", boxStyle[1]);
		box[2] = new CheckBox("", boxStyle[2]);

		but[0] = new ImageButton(new TextureRegionDrawable(buttons.findRegion(
				"new", 0)), new TextureRegionDrawable(buttons.findRegion("new",
				1)));
		but[1] = new ImageButton(new TextureRegionDrawable(buttons.findRegion(
				"zoomin", 0)), new TextureRegionDrawable(buttons.findRegion(
				"zoomin", 1)));
		but[2] = new ImageButton(new TextureRegionDrawable(buttons.findRegion(
				"zoomout", 0)), new TextureRegionDrawable(buttons.findRegion(
				"zoomout", 1)));
		TextureRegion t = new TextureRegion(buttons.findRegion("fast", 0));
		TextureRegion t1 = new TextureRegion(buttons.findRegion("fast", 1));
		t.flip(true, false);
		t1.flip(true, false);
		but[3] = new ImageButton(new TextureRegionDrawable(t),
				new TextureRegionDrawable(t1));
		but[4] = new ImageButton(new TextureRegionDrawable(buttons.findRegion(
				"restart", 0)), new TextureRegionDrawable(buttons.findRegion(
				"restart", 1)));
		t = buttons.findRegion("fast", 0);
		t1 = buttons.findRegion("fast", 1);
		but[5] = new ImageButton(new TextureRegionDrawable(t),
				new TextureRegionDrawable(t1));
		but[6] = new ImageButton(new TextureRegionDrawable(buttons.findRegion(
				"exit", 0)), new TextureRegionDrawable(buttons.findRegion(
				"exit", 1)));

		table.add(but[0]).expand();
		table.add(but[1]).expand();
		table.add(but[2]).expand();
		table.add(but[3]).expand();
		table.add(but[4]).expand();
		table.add(box[0]).expand();
		table.add(box[1]).expand();
		table.add(but[5]).expand();
		table.add(box[2]).expand();
		table.add(but[6]).expand();

		// add
		but[0].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
			};
		});
		// zoom in
		but[1].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Constant.ZOOM += 0.5f * Constant.ZOOM;
			};
		});
		// zoom out
		but[2].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Constant.ZOOM -= 0.5f * Constant.ZOOM;
			};
		});
		// slow down
		but[3].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Constant.GAME_SPEED -= 0.5f * Constant.GAME_SPEED;
			};
		});
		// restart
		but[4].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				ng.restart();
			};
		});
		// pause
		box[0].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Constant.GAME_STATE = false;
				if (!box[0].isChecked()) {
					box[0].setChecked(true);
				}
				box[1].setChecked(!box[0].isChecked());
			};
		});
		// play
		box[1].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Constant.GAME_STATE = true;
				if (!box[1].isChecked()) {
					box[1].setChecked(true);
				}
				box[0].setChecked(!box[1].isChecked());
			};
		});
		// fast
		but[5].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Constant.GAME_SPEED += 0.5f * Constant.GAME_SPEED;
			};
		});
		// mute
		box[2].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Constant.VOLUME = !Constant.VOLUME;
			};
		});
		// hide
		but[6].addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
			};
		});
		table.setSize(w, 80);
		table.setPosition(0, h - 80);
		return table;
	}

	public void dispose() {
		skin.dispose();
		buttons.dispose();
	}
}
