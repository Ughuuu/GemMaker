package com.gemengine.engine;

import org.apache.logging.log4j.MarkerManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.GL20;
import com.gemengine.system.AssetSystem;
import com.gemengine.system.ComponentSystem;
import com.gemengine.system.EntitySystem;
import com.gemengine.system.ManagerSystem;
import com.gemengine.system.manager.SystemManager;

import lombok.extern.log4j.Log4j2;

/**
 * This class contains the system manager and handles the application life cycle by extending {@link ApplicationListener}
 * 
 * @author Dragos
 * 
 */
@Log4j2
public class Gem implements ApplicationListener {
	/**
	 * System Manager is owned by the gem, certain events are given from here.
	 */
	private final SystemManager systemManager;

	/**
	 * Construct a new instance of the game engine.
	 * 
	 * @param configuration
	 *            Configuration for the game engine.
	 */
	public Gem(GemConfiguration configuration) {
		systemManager = new SystemManager(configuration);
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void create() {
		try {
			systemManager.replaceType(AssetSystem.class);
			systemManager.replaceType(ComponentSystem.class);
			systemManager.replaceType(EntitySystem.class);
			systemManager.replaceType(ManagerSystem.class);
			systemManager.onInit();
		} catch (Throwable t) {
			log.fatal(MarkerManager.getMarker("gem"), "Basic Systems create", t);
		}
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void dispose() {
		systemManager.onDispose();
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void pause() {
		systemManager.onPause();
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void render() {
		Gdx.gl.glClearColor(0.262f, 0.129f, 0.262f, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		try {
			systemManager.onUpdate(Gdx.graphics.getDeltaTime());
		} catch (Throwable t) {
			log.fatal(MarkerManager.getMarker("gem"), "System Manager updating", t);
		}
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void resize(int width, int height) {
		systemManager.onResize(width, height);
	}

	@Override
	/**
	 * {@inheritDoc}
	 */
	public void resume() {
		systemManager.onResume();
	}
}
