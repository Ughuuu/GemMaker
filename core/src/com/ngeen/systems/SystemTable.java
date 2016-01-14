package com.ngeen.systems;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.ngeen.component.ComponentUILayout;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class SystemTable extends SystemBase{	
	public SystemTable(Ngeen ng, SystemConfiguration conf) {
		super(ng, conf);
	}

	@Override
	public void onUpdate(Entity ent){
		//ent.getComponent(ComponentUILayout.class).act(deltaTime);
	}
}
