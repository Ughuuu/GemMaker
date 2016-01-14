package com.ngeen.systems;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.ngeen.component.ComponentUILayout;
import com.ngeen.component.ComponentUIStage;
import com.ngeen.component.ComponentUIWidget;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

public class SystemStage  extends SystemBase{
	
	public SystemStage(Ngeen ng, SystemConfiguration conf) {
		super(ng, conf);
	}

	Set<Entity> FirstUINodes(Entity ent){
		Set<Entity> children = ent.getChildren();
		for(Entity child : children){
			if(!child.hasComponent(ComponentUILayout.class) &&
					!child.hasComponent(ComponentUIWidget.class)){
				children.remove(child);
				Set<Entity> subChildren = child.getChildren();
				for(Entity subChild : subChildren){
					children.addAll(FirstUINodes(subChild));
				}
				children.addAll(subChildren);
			}
		}
		return children;
	}
	
	private void resolveUI(Entity ent){
		if(ent.hasComponent(ComponentUILayout.class)){
			
		}
		if(ent.hasComponent(ComponentUIWidget.class)){
			
		}
	}
	
	@Override
	public void onUpdate(Entity ent){
		ComponentUIStage stage = ent.getComponent(ComponentUIStage.class).act();
		Set<Entity> entities = FirstUINodes(ent);
		for(Entity child : entities){
			resolveUI(child);
		}
	}
}