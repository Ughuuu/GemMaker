package com.gemengine.engine;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.gemengine.system.AssetSystem;
import com.gemengine.system.ComponentSystem;
import com.gemengine.system.EntitySystem;
import com.gemengine.system.ManagerSystem;
import com.gemengine.system.manager.SystemManager;

public class Gem implements ApplicationListener {
	private final SystemManager systemManager;

	public Gem(GemConfiguration configuration) {
		systemManager = new SystemManager(configuration);
	}

	@Override
	public void create() {
		systemManager.replaceType(AssetSystem.class);
		systemManager.replaceType(ComponentSystem.class);
		systemManager.replaceType(EntitySystem.class);
		systemManager.replaceType(ManagerSystem.class);
		try {
			systemManager.onInit();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public void dispose() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		try {
			systemManager.onUpdate(Gdx.graphics.getDeltaTime());
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void resume() {
	}
}
