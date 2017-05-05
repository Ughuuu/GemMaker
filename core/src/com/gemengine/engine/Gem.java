package com.gemengine.engine;

import org.apache.logging.log4j.MarkerManager;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.gemengine.system.AssetSystem;
import com.gemengine.system.ComponentSystem;
import com.gemengine.system.EntitySystem;
import com.gemengine.system.ManagerSystem;
import com.gemengine.system.manager.SystemManager;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class Gem implements ApplicationListener {
	private final SystemManager systemManager;

	public Gem(GemConfiguration configuration) {
		systemManager = new SystemManager(configuration);
	}

	@Override
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
	public void dispose() {
		log.debug(MarkerManager.getMarker("gem"), "Gem dispose event");
	}

	@Override
	public void pause() {
		log.debug(MarkerManager.getMarker("gem"), "Gem pause event");
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		try {
			systemManager.onUpdate(Gdx.graphics.getDeltaTime());
		} catch (Throwable t) {
			log.fatal(MarkerManager.getMarker("gem"), "System Manager updating", t);
		}
	}

	@Override
	public void resize(int width, int height) {
		log.debug(MarkerManager.getMarker("gem"), "Gem resize event with width {} and height {}", width, height);
	}

	@Override
	public void resume() {
		log.debug(MarkerManager.getMarker("gem"), "Gem resume event");
	}
}
