package com.ngeen.scene;

import com.ngeen.debug.Debugger;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class LoadScene extends Scene {
	@Override
	public void onInit() {
	};

	@Override
	public void onUpdate(float delta) {
		ng.Loader.finish();//blocking load
		//changeScene("TestScene");
	}
	
	@Override
	public void onExit(){
		
	}
}
