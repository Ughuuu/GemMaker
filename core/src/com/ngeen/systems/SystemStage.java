package com.ngeen.systems;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.ngeen.component.ComponentBase;
import com.ngeen.component.ComponentUILayout;
import com.ngeen.component.ComponentUIStage;
import com.ngeen.component.ComponentUIWidget;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.Ngeen;
import com.ngeen.engine.TypeObserver;
import com.ngeen.entity.Entity;

public class SystemStage  extends SystemBase implements TypeObserver<ComponentBase>{
	private List<ComponentBase> _ChangedComponent;
	private List<ComponentBase> _AddedComponent;
	private List<ComponentBase> _RemovedComponent;
	
	public SystemStage(Ngeen ng, SystemConfiguration conf) {
		super(ng, conf);
		_AddedComponent = new ArrayList<ComponentBase>();
		_ChangedComponent = new ArrayList<ComponentBase>();
		_RemovedComponent = new ArrayList<ComponentBase>();
	}

	List<Entity> FirstUINodes(Entity ent){
		List<Entity> children = ent.getChildren();
		for(Entity child : children){
			if(!child.hasComponent(ComponentUILayout.class) &&
					!child.hasComponent(ComponentUIWidget.class)){
				children.remove(child);
				List<Entity> subChildren = child.getChildren();
				for(Entity subChild : subChildren){
					children.addAll(FirstUINodes(subChild));
				}
				children.addAll(subChildren);
			}
		}
		return children;
	}
	
	private boolean isUIType(Entity ent){
		return ent.hasComponent(ComponentUILayout.class) || ent.hasComponent(ComponentUILayout.class);
	}
	
	@Override
	public void onUpdate(Entity ent){
		ComponentUIStage stage = ent.getComponent(ComponentUIStage.class).act();
		stage.act();
	}

	@Override
	public void Removed(ComponentBase obj) {
		_RemovedComponent.add(obj);
	}

	@Override
	public void Added(ComponentBase obj) {
		_AddedComponent.add(obj);
	}

	@Override
	public void Changed(ComponentBase obj) {
		//workaround, change this
		_RemovedComponent.add(obj);
		_AddedComponent.add(obj);
	}
}