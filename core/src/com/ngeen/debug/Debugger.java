package com.ngeen.debug;

import java.util.HashMap;
import java.util.Map;

import com.artemis.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ngeen.engine.Constant;
import com.ngeen.engine.Ngeen;

public class Debugger extends Stage {
	public Skin skin;
	public Spy spy;
	final Object obj;

	public Debugger(final Object obj, Ngeen ng) {
		this.obj = ng;
		skin = new Skin(Gdx.files.internal("debug\\darkui.json"));
		skin.getFont("default-font").getData().setScale(0.5f);
		spy = new Spy(skin, obj);
		spy.table.setPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getWidth() / 4);
		this.addActor(spy.table);
	}

	public static void print(String s) {

	}

	public static void println(String s) {

	}

	public static void addEntity(Entity e) {

	}

	public static void changeEntity(Entity e) {

	}

	public static void deleteEntity(Entity e) {

	}

}
