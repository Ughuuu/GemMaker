package com.ngeen.scene;

import com.badlogic.gdx.math.Vector2;

public class LoadScene extends Scene implements SceneInterface {
	
	public void onInit() {
		ng.Loader.finish();//blocking load
		ng.changeScene("TestScene");
	}
}
