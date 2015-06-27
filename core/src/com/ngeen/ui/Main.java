package com.ngeen.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.ngeen.holder.Constant;
import com.ngeen.holder.Ngeen;
import com.ngeen.tester.Test;

public class Main extends Game {
	Interface stage;
	Ngeen ng;
	Test test;

	@Override
	public void create() {
		ng = new Ngeen();
		stage = new Interface(ng);
		test = new Test(ng);

		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(Constant.BACKGROUND_COLOR.r, Constant.BACKGROUND_COLOR.g, Constant.BACKGROUND_COLOR.b, Constant.BACKGROUND_COLOR.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		ng.update(Gdx.graphics.getDeltaTime());
	}
}
