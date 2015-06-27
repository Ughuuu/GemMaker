package com.ngeen.systems;

import com.artemis.annotations.Wire;
import com.artemis.systems.VoidEntitySystem;
import com.badlogic.gdx.Gdx;
import com.ngeen.scene.LoadScene;
import com.ngeen.scene.Scene;

@Wire
public class SceneSystem extends VoidEntitySystem {
	private Scene scene;

	public SceneSystem(){
		this.scene = new LoadScene();
	}
	
	public Scene getScene() {
		return scene;
	}

	public void setScene(Scene scene) {
		this.scene = scene;
	}

	@Override
	protected void processSystem() {
		scene.onUpdate(Gdx.graphics.getDeltaTime());
	}

}
