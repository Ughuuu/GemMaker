package com.ngeen.systems;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class SystemWidget extends SystemBase{
	
	public SystemWidget(Ngeen ng, SystemConfiguration conf) {
		super(ng, conf);
	}

	@Override
	public void onUpdate(Entity ent){
		//ent.getComponent(ComponentUILayout.class).act(deltaTime);
	}
}
