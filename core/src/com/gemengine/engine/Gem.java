package com.gemengine.engine;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.gemengine.system.AssetSystem;
import com.gemengine.system.ComponentSystem;
import com.gemengine.system.ManagerSystem;
import com.gemengine.system.helper.SystemManager;

public abstract class Gem implements ApplicationListener{
	private final SystemManager systemManager;

	public Gem(){
		systemManager = new SystemManager();
	}
	
	@Override
	public void create() {
		systemManager.addType(AssetSystem.class);
		systemManager.addType(ManagerSystem.class);
		systemManager.addType(ComponentSystem.class);
		systemManager.onInit();
		System.out.println("ok");
	}

	@Override
	public void dispose() {
	}

	public abstract String getEntry();

	@Override
	public void pause() {
	}

	@Override
	public void render() {
		Gdx.gl.glClearColor(0, 0, 0, 0);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		systemManager.onUpdate(Gdx.graphics.getDeltaTime());
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void resume() {
	}
}
