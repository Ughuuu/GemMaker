package com.ngeen.ui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.ngeen.holder.Ngeen;

public class Main extends Game {
	Interface stage;
	Ngeen ng;

	@Override
	public void create() {
		ng = new Ngeen();
		stage = new Interface(ng);

		Gdx.input.setInputProcessor(stage);
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0.7f, 0.7f, 0.7f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		stage.act(Gdx.graphics.getDeltaTime());
		stage.draw();
		ng.update(Gdx.graphics.getDeltaTime());
	}
}
