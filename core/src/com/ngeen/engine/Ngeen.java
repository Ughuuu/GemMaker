package com.ngeen.engine;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.input.GestureDetector.GestureListener;
import com.badlogic.gdx.utils.TimeUtils;
import com.ngeen.asset.AssetFactory;
import com.ngeen.component.ComponentFactory;
import com.ngeen.asset.AssetFactory;
import com.ngeen.entity.Entity;
import com.ngeen.entity.EntityFactory;
import com.ngeen.scene.Scene;
import com.ngeen.scene.SceneFactory;
import com.ngeen.systems.SceneSystem;
import com.ngeen.systems.SystemBase;

/**
 * Main engine class. Links all elements and holds entities.
 * 
 * @author Dragos
 *
 */
public class Ngeen extends ApplicationAdapter {
	private GestureListener _EditorInput;
	public SceneSystem SceneSystem;
	public AssetFactory Loader;
	public EntityFactory EntityBuilder;
	public SceneFactory SceneBuilder;
	public ComponentFactory ComponentBuilder;

	public void init() {
		_EditorInput = new Editor(this);
		Loader = new AssetFactory(this);
		EntityBuilder = new EntityFactory(this);
		SceneBuilder = new SceneFactory(this);
		ComponentBuilder = new ComponentFactory(this);
		SceneSystem = new SceneSystem(this, "LoadScene");
		final InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor((InputProcessor) _EditorInput);
		multiplexer.addProcessor(new GestureDetector(SceneSystem));
		Gdx.input.setInputProcessor(multiplexer);
	}

	private void updateSystem(SystemBase system) {
		float time = TimeUtils.millis();
		system.onBeforeUpdate();
		system.onAfterUpdate();
		system.deltaTime = TimeUtils.millis() - time;
	}

	public void update(float delta) {
		Loader.done();
		updateSystem(SceneSystem);
	}

	public void restart() {
	}

	@Override
	public void create() {
		init();
		Loader.scoutFiles();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(Constant.BACKGROUND_COLOR.r, Constant.BACKGROUND_COLOR.g, Constant.BACKGROUND_COLOR.b,
				Constant.BACKGROUND_COLOR.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void resize(int w, int h) {
	}

	public void dispose() {
		// Constant.BATCH.dispose();
	}
}
