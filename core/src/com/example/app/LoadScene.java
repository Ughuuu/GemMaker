package com.example.app;

import com.ngeen.scene.Scene;

public class LoadScene extends Scene {
	@Override
	public void onInit() {
		ng.Loader.scoutFiles();
		ng.Loader.finish();// blocking load
		ng.changeScene("com.example.app.TestScene");
	}
}
