package com.ngeen.scene.implemented;

import com.ngeen.debug.Debugger;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;
import com.ngeen.scene.Scene;

public class LoadScene extends Scene {

	public LoadScene(Ngeen ng) {
		super(ng);
	}

	@Override
	public void onInit() {
	};

	@Override
	public void onUpdate(float delta) {
		Entity ent = ng.EntityBuilder.makeEntity("~CAMERA");
	}
}
