package com.ngeen.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.plaf.basic.BasicIconFactory;

import com.badlogic.gdx.utils.XmlWriter;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.ngeen.asset.Asset;
import com.ngeen.component.*;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.EngineInfo;
import com.ngeen.engine.Ngeen;

public class Entity {
	protected final Ngeen _Ng;
	private final ComponentFactory _ComponentFactory;
	public String Name;
	protected int Id, Parent = -1;
	private static int _Unique_id = 0;
	private List<Integer> _Children;
	private static final List emptyList = new ArrayList();
	private BitSet _Configuration = new BitSet(EngineInfo.TotalComponents);
	
	private Map<Class<?>, Map<Integer, ComponentBase>> _ComponentMap;

	public Entity(Ngeen ng, ComponentFactory componentBuilder, String name) {
		_Ng = ng;
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
		_Ng.EntityBuilder.removeEntity(this);
	}
	
	public Set<Class<?>> getComponentTypes(){
		return _ComponentMap.keySet();
	}
	
	public BitSet getConfiguration(){
		return _Configuration;
	}

	public <T extends ComponentBase> T addComponent(Class<T> type) {
		_Ng.EntityBuilder.treeRemoveObject(this);
		
		ComponentBase component = _ComponentFactory.createComponent(type, this);
		Map<Integer, ComponentBase> list = null;
		if (_ComponentMap.containsKey(component.getClass()) == false) {
			list = new HashMap<Integer, ComponentBase>();
			_ComponentMap.put(component.getClass(), list);
		} else {
			list = _ComponentMap.get(component.getClass());
		}
		list.put(component.getId(), component);
				
		_Configuration.set(EngineInfo.ComponentIndexMap.get(type));
		
		_Ng.EntityBuilder.treeAddObject(this);
		
		return (T) component;
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
		_Ng.EntityBuilder.treeRemoveObject(this);
		
		Map<Integer, ComponentBase> componentMap = _ComponentMap.get(type);
		if(componentMap == null){
			_Ng.EntityBuilder.treeAddObject(this);
			return;
		}
		ComponentBase component = componentMap.remove(id);
		_ComponentFactory.removeComponent(type, component);

		if(componentMap.size()==0){
			_Configuration.clear(EngineInfo.ComponentIndexMap.get(type));
			_ComponentMap.remove(type);
		}
		
		_Ng.EntityBuilder.treeAddObject(this);
	}
	
	public <T extends ComponentBase> void removeComponent(Class<T> type){
		_Ng.EntityBuilder.treeRemoveObject(this);
		
		Map<Integer, ComponentBase> components = _ComponentMap.get(type);
		if(components == null){
			_Ng.EntityBuilder.treeAddObject(this);
			return;
		}
		for(Map.Entry<Integer, ComponentBase> iter : components.entrySet()){
			_ComponentFactory.removeComponent(type, iter.getValue());
		}
		_ComponentMap.get(type).clear();
		_ComponentMap.remove(type);
		
		_Configuration.clear(EngineInfo.ComponentIndexMap.get(type));
		
		_Ng.EntityBuilder.treeAddObject(this);
	}
	
	public void clearComponents(){		
		for(Map.Entry<Class<?>, Map<Integer, ComponentBase>> typeMap : _ComponentMap.entrySet()){
			Map<Integer, ComponentBase> components = typeMap.getValue();
			Class<?> type = typeMap.getKey();
			for(Map.Entry<Integer, ComponentBase> iter : components.entrySet()){
				_ComponentFactory.removeComponent(type, iter.getValue());
			}
		}
		_ComponentMap.clear();
		_Configuration.clear();
	}
	
	public <T extends ComponentBase> boolean hasComponent(Class<T> type){
		return _ComponentMap.containsKey(type);
	}
	
	public int getId(){
		return Id;
	}


	protected void Save(XmlWriter element, XmlComponent _XmlComponent) throws Exception {
		element.element("Entity").
		attribute("Name", Name).
		attribute("Parent", Parent);		
		for(Map.Entry<Class<?>, Map<Integer, ComponentBase>> ComponentsIndexMap : _ComponentMap.entrySet()){
			for(Map.Entry<Integer, ComponentBase> Components : ComponentsIndexMap.getValue().entrySet()){
				_XmlComponent.Save(Components.getValue(), element);
			}
		}		
		element.pop();
	}

	private ComponentBase addComponentUnsafe(Class<?> type) {
		_Ng.EntityBuilder.treeRemoveObject(this);
		
		ComponentBase component = _ComponentFactory.createComponent(type, this);
		Map<Integer, ComponentBase> list = null;
		if (_ComponentMap.containsKey(component.getClass()) == false) {
			list = new HashMap<Integer, ComponentBase>();
			_ComponentMap.put(component.getClass(), list);
		} else {
			list = _ComponentMap.get(component.getClass());
		}
		list.put(component.getId(), component);
				
		_Configuration.set(EngineInfo.ComponentIndexMap.get(type));
		
		_Ng.EntityBuilder.treeAddObject(this);
		
		return component;
	}
	
	protected void Load(Element element, XmlComponent _XmlComponent) throws Exception {
		Parent = element.getInt("Parent");
		for (Element el : element.getChildrenByName("Component")) {
			String type = el.get("_Type");
			
			Class<?> cls = Class.forName("com.ngeen.component."+type);

			_XmlComponent.Load(addComponentUnsafe(cls), el);
		}
	}
}
