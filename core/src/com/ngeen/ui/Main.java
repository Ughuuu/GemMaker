package com.ngeen.ui;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.ngeen.holder.Constant;
import com.ngeen.holder.Ngeen;
import com.ngeen.scene.LoadScene;
import com.ngeen.tester.Test;

public class Main extends ApplicationAdapter {
	Interface stage;
	Ngeen ng;
	Test test;

	void initDisplay() {
		stage = new Interface(ng);

		final InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor(stage);
		multiplexer.addProcessor(ng.getInputProcessor());
		multiplexer.addProcessor(new GestureDetector(ng.getGestureListener()));
		Gdx.input.setInputProcessor(multiplexer);
	}

	@Override
	public void create() {
		ng = new Ngeen();
		initDisplay();
		test = new Test(ng);
		ng.setScene(new LoadScene());
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(Constant.BACKGROUND_COLOR.r,
				Constant.BACKGROUND_COLOR.g, Constant.BACKGROUND_COLOR.b,
				Constant.BACKGROUND_COLOR.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		ng.update(Gdx.graphics.getDeltaTime());
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
	}

	@Override
	public void resize(int w, int h) {
		initDisplay();
	}
}
