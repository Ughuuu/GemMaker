package com.ngeen.entity;

import java.util.List;
import java.util.Map;

import com.ngeen.components.BaseComponent;

public class Entity {
	public Map<Class<T extends BaseComponent>, BaseComponent> componentMap;
	
	public <T extends BaseComponent> T getComponent(Class<T> cls){
		if()
		return null;		
	}
}
