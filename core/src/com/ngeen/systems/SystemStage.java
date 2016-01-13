package com.ngeen.systems;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.ngeen.component.ComponentUIStage;
import com.ngeen.component.ui.layout.ComponentUIStack;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class SystemStage  extends SystemBase{
	
	public SystemStage(Ngeen ng, SystemConfiguration conf) {
		super(ng, conf);
	}

	List<Entity> FirstUINodes(Entity ent){
		return ret;
	}
	
	@Override
	public void onUpdate(Entity ent){
		ComponentUIStage stage = ent.getComponent(ComponentUIStage.class).act();
		
	}
}