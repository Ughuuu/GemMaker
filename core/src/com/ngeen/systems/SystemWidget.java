package com.ngeen.systems;

import javax.swing.plaf.ComponentUI;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.ngeen.component.ComponentUIWidget;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class SystemWidget extends SystemBase{
	
	public SystemWidget(Ngeen ng, SystemConfiguration conf) {
		super(ng, conf);
	}

	@Override
	public void onUpdate(Entity ent){
		//ent.getComponent(ComponentUIWidget.class).act(0.16f);
	}
}
