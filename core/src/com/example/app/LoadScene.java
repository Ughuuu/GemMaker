package com.example.app;

import com.gem.scene.Scene;

public class LoadScene extends Scene {
	@Override
	public void onInit() {
		gem.Loader.scoutFiles();
		gem.Loader.finish();// blocking load
		gem.changeScene("com.example.app.Main");
	}
}
