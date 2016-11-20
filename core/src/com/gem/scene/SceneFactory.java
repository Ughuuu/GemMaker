package com.gem.scene;

import com.badlogic.gdx.Gdx;
import com.gem.engine.Gem;
import com.gem.systems.SystemScene;

/**
 * @author Dragos
 * @composed 1 - 1 Scene
 */
public class SceneFactory {
	private final Gem gem;
	private final SystemScene sceneSystem;

	public SceneFactory(Gem gem, SystemScene sceneSystem) {
		this.gem = gem;
		this.sceneSystem = sceneSystem;
	}

	public void changeScene(String name) {
		Gdx.graphics.setTitle(name);
		Scene scene = makeScene(name);
		scene.name = name;
		sceneSystem.setScene(scene);
	}

	public Scene makeScene(String name) {
		try {
			Class<?> scene = Class.forName(name);
			Scene scn = (Scene) scene.newInstance();
			scn.addGem(gem);
			scn.addSceneFactory(this);
			return scn;
		} catch (Exception e) {
			// You don't have to have a scene class for the scene xml, it's
			// optional
			// e.printStackTrace();
			// Debugger.log(e.t);
		}
		return new Scene();
	}
}
