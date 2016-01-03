package com.ngeen.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ngeen.components.BaseComponent;
import com.ngeen.engine.Ngeen;

public class Entity {
	protected String name;
	protected int id, parent = -1;
	private static int unique_id = 0;

	private Map<Class<?>, BaseComponent> componentMap;

	public Entity(){
		id = unique_id++;
		componentMap = new HashMap<Class<?>, BaseComponent>();
	}
	
	public <T extends BaseComponent> T getComponent(Class<T> cls){
		return (T) componentMap.get(cls);
	}
}
