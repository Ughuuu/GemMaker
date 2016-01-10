package com.ngeen.scene;

import com.badlogic.gdx.Gdx;
import com.ngeen.debug.TestAsset;
import com.ngeen.debug.TestComponents;
import com.ngeen.debug.TestEntity;
import com.ngeen.debug.TestSystemConfiguration;
import com.ngeen.engine.Ngeen;

public class TestScene extends Scene{
	
	@Override
	public void onInit(){
		new TestSystemConfiguration(ng);
		new TestAsset(ng);
		new TestComponents(ng);
		new TestEntity(ng);
	}
	
	@Override
	public void onUpdate(float delta) {
			Gdx.app.exit();
	}

}
