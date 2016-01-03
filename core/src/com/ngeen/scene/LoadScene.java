package com.ngeen.scene;

import com.ngeen.components.CameraComponent;
import com.ngeen.entity.Entity;

public class LoadScene extends Scene {

	@Override
	public void onInit() {
		ng.load("");
	};

	@Override
	public void onUpdate(float delta) {
		Entity e = null;
		CameraComponent c = e.getComponent(CameraComponent.class);
	}
}
