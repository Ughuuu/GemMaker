package com.ngeen.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ngeen.component.ComponentBase;
import com.ngeen.component.ComponentFactory;
import com.ngeen.engine.Ngeen;

public class Entity {
	protected final Ngeen Ng;
	private final ComponentFactory _ComponentFactory;
	public String Name;
	protected int Id, Parent = -1;
	private static int _Unique_id = 0;
	private List<Integer> _Children;
	private static final List emptyList = new ArrayList();

	private Map<Class<?>, Map<Integer, ComponentBase>> _ComponentMap;

	public Entity(Ngeen ng, ComponentFactory componentBuilder, String name) {
		Ng = ng;
		_ComponentFactory = componentBuilder;
		this.Name = name;
		Id = _Unique_id++;
		_ComponentMap = new HashMap<Class<?>, Map<Integer, ComponentBase>>();
		_Children = new ArrayList<Integer>();
	}
	
	protected void reset(String name){
		Id = _Unique_id++;
		this.Name = name;
	}
	
	public void remove(){
		Ng.EntityBuilder.removeEntity(this);
	}

	public <T extends ComponentBase> T addComponent(Class<T> type) {
		Ng.EntityBuilder.treeRemoveObject(this);
		
		ComponentBase component = _ComponentFactory.createComponent(type, this);
		Map<Integer, ComponentBase> list = null;
		if (_ComponentMap.containsKey(component.getClass()) == false) {
			list = new HashMap<Integer, ComponentBase>();
			_ComponentMap.put(component.getClass(), list);
		} else {
			list = _ComponentMap.get(component.getClass());
		}
		list.put(component.getId(), component);
		
		Ng.EntityBuilder.treeAddObject(this);
		
		return (T) component;
	}
	
	public Set<Class<?>> getComponentTypes(){
		return _ComponentMap.keySet();
		
	}
	
	public <T extends ComponentBase> List<T> getComponents(Class<T> type){
		Map<Integer, ComponentBase> componentMap = _ComponentMap.get(type);
		if(componentMap == null)
			return emptyList;
		return new ArrayList<T>((Collection<? extends T>) (componentMap.values()));// buggy here
	}
	
	public <T extends ComponentBase> T getComponent(Class<T> type){
		Map<Integer, ComponentBase> componentMap = _ComponentMap.get(type);
		if(componentMap == null)
			return null;
		return (T) componentMap.entrySet().iterator().next().getValue();
	}
	
	public <T extends ComponentBase> T getComponent(Class<T> type, int id){
		Map<Integer, ComponentBase> componentMap = _ComponentMap.get(type);
		if(componentMap == null)
			return null;
		return (T) componentMap.get(id);
	}
	
	public <T extends ComponentBase> void removeComponent(Class<T> type, int id){
		Ng.EntityBuilder.treeRemoveObject(this);
		
		Map<Integer, ComponentBase> componentMap = _ComponentMap.get(type);
		if(componentMap == null){
			Ng.EntityBuilder.treeAddObject(this);
			return;
		}
		ComponentBase component = componentMap.remove(id);
		_ComponentFactory.removeComponent(type, component);
		
		Ng.EntityBuilder.treeAddObject(this);
	}
	
	public <T extends ComponentBase> void removeComponent(Class<T> type){
		Ng.EntityBuilder.treeRemoveObject(this);
		
		Map<Integer, ComponentBase> components = _ComponentMap.get(type);
		if(components == null){
			Ng.EntityBuilder.treeAddObject(this);
			return;
		}
		for(Map.Entry<Integer, ComponentBase> iter : components.entrySet()){
			_ComponentFactory.removeComponent(type, iter.getValue());
		}
		_ComponentMap.get(type).clear();
		_ComponentMap.remove(type);
		
		Ng.EntityBuilder.treeAddObject(this);
	}
	
	public void clearComponents(){		
		for(Map.Entry<Class<?>, Map<Integer, ComponentBase>> typeMap : _ComponentMap.entrySet()){
			Map<Integer, ComponentBase> components = typeMap.getValue();
			Class<?> type = typeMap.getKey();
			for(Map.Entry<Integer, ComponentBase> iter : components.entrySet()){
				_ComponentFactory.removeComponent(type, iter.getValue());
			}
		}
	}
	
	public <T extends ComponentBase> boolean hasComponent(Class<T> type){
		return _ComponentMap.containsKey(type);
	}
	
	public int getId(){
		return Id;
	}
}
