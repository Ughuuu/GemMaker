package com.ngeen.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ngeen.component.ComponentBase;
import com.ngeen.engine.Ngeen;

public class Entity {
	protected final Ngeen _Ng;
	protected String name;
	protected int id, parent = -1;
	private static int unique_id = 0;
	private List<Integer> children;

	private Map<Class<?>, List<ComponentBase>> componentMap;

	public Entity(Ngeen ng, String name) {
		_Ng = ng;
		this.name = name;
		id = unique_id++;
		componentMap = new HashMap<Class<?>, List<ComponentBase>>();
		children = new ArrayList<Integer>();
	}
	
	public void remove(){
		_Ng.EntityBuilder.removeEntity(this);
	}

	public <T extends ComponentBase> void addComponent(Class<?> cls) {
		ComponentBase component = _Ng.ComponentBuilder.createComponent(cls);
		List<ComponentBase> list = null;
		if (componentMap.containsKey(component.getClass()) == false) {
			list = new ArrayList<ComponentBase>();
			componentMap.put(component.getClass(), list);
		} else {
			list = componentMap.get(component.getClass());
		}
		list.add(component);
	}
	
	public <T extends ComponentBase> List<T> getComponents(Class<?> cls){
		return (List<T>) componentMap.get(cls);
	}
	
	public <T extends ComponentBase> T getComponent(Class<?> cls){
		return (T) componentMap.get(cls).get(0);
	}
	
	public <T extends ComponentBase> void removeComponent(Class<?> type, int id){
		componentMap.get(type)		
	}
}
