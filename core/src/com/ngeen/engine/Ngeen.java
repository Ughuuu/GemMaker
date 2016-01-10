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
import com.ngeen.entity.EntityFactory;
import com.ngeen.asset.AssetFactory;
import com.ngeen.scene.Scene;
import com.ngeen.scene.SceneFactory;
import com.ngeen.systems.SystemOverlay;
import com.ngeen.systems.SceneSystem;
import com.ngeen.systems.SystemBase;
import com.ngeen.systems.SystemConfiguration;

/**
 * Main engine class. Links all elements and holds entities.
 * 
 * @author Dragos
 *
 */
public class Ngeen extends ApplicationAdapter {
	public AssetFactory Loader;
	public EntityFactory EntityBuilder;
	
	protected SceneFactory SceneBuilder;
	private GestureListener _EditorInput;
	protected ComponentFactory ComponentBuilder;	
	private SceneSystem _SceneSystem;
	private SystemOverlay _OverlaySystem;

	public void init() {
		_EditorInput = new Editor(this);
		Loader = new AssetFactory(this);
		ComponentBuilder = new ComponentFactory(this);
		EntityBuilder = new EntityFactory(this, ComponentBuilder);
		
		_OverlaySystem = new SystemOverlay(this, new SystemConfiguration().all());
		_SceneSystem = new SceneSystem(this);
		
		SceneBuilder = new SceneFactory(this, _SceneSystem);
		SceneBuilder.changeScene("LoadScene");
		final InputMultiplexer multiplexer = new InputMultiplexer();
		multiplexer.addProcessor((InputProcessor) _EditorInput);
		multiplexer.addProcessor(new GestureDetector(_SceneSystem));
		multiplexer.addProcessor((InputProcessor)_OverlaySystem);
		Gdx.input.setInputProcessor(multiplexer);
		
		EngineInfo.makeBasicEntities(this);
	}

	private void updateSystem(SystemBase system) {
		float time = TimeUtils.millis();
		system.onBeforeUpdate();
		system.onAfterUpdate();
		system.deltaTime = TimeUtils.millis() - time;
	}

	public void update(float delta) {
		Loader.done();
		updateSystem(_SceneSystem);
		updateSystem(_OverlaySystem);
	}

	public void restart() {
		remove();
	}

	@Override
	public void create() {
		init();
		Loader.scoutFiles();
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(EngineInfo.BackgroundColor.r, EngineInfo.BackgroundColor.g, EngineInfo.BackgroundColor.b,
				EngineInfo.BackgroundColor.a);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		update(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void resize(int w, int h) {
	}

	public void remove() {
		EntityBuilder.clear();
		ComponentBuilder.clear();
	}
}
