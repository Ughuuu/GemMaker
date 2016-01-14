package com.ngeen.entity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeSet;

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
	protected String Name;
	protected int Id;
	//protected boolean _Update = false;
	protected Entity Parent;
	protected String _ParentName = "null";
	private static int _Unique_id = 0;
	private Set<Entity> _Children;
	private static final List emptyList = new ArrayList();
	private BitSet _Configuration = new BitSet(EngineInfo.TotalComponents);

	private Map<Class<?>, Map<Integer, ComponentBase>> _ComponentMap;

	public Entity(Ngeen ng, ComponentFactory componentBuilder, String name) {
		_Ng = ng;
		_ComponentFactory = componentBuilder;
		this.Name = name;
		Id = _Unique_id++;
		_ComponentMap = new HashMap<Class<?>, Map<Integer, ComponentBase>>();
		_Children = new TreeSet<Entity>(new Comparator<Entity>() {
			@Override
			public int compare(Entity o1, Entity o2) {
				return Integer.signum(o1.hashCode() - o2.hashCode());
			}
		});
	}

	protected void reset(String name) {
		Id = _Unique_id++;
		this.Name = name;
	}

	public void remove() {
		_Ng.EntityBuilder.removeEntity(this);
	}

	public Set<Class<?>> getComponentTypes() {
		return _ComponentMap.keySet();
	}

	public BitSet getConfiguration() {
		return _Configuration;
	}

	public <T extends ComponentBase> T addComponent(Class<T> type) {
		_Ng.EntityBuilder.treeRemoveObject(this);

		if (_ComponentMap.containsKey(type)) {
			_Ng.EntityBuilder.treeAddObject(this);
			return (T) _ComponentMap.get(type).entrySet().iterator().next().getValue();
		}

		ComponentBase component = _ComponentFactory.createComponent(type, this);
		Map<Integer, ComponentBase> list = null;
		if (_ComponentMap.containsKey(type) == false) {
			list = new HashMap<Integer, ComponentBase>();
			_ComponentMap.put(type, list);
		} else {
			list = _ComponentMap.get(type);
		}
		list.put(component.getId(), component);

		_Configuration.set(EngineInfo.ComponentIndexMap.get(type));

		_Ng.EntityBuilder.treeAddObject(this);

		return (T) component;
	}

	public <T extends ComponentBase> T addSuperComponent(ComponentBase component) {
		Class<?> type = component.getClass().getSuperclass();
		_Ng.EntityBuilder.treeRemoveObject(this);

		if (_ComponentMap.containsKey(type)) {
			_Ng.EntityBuilder.treeAddObject(this);
			return (T) _ComponentMap.get(type).entrySet().iterator().next().getValue();
		}

		Map<Integer, ComponentBase> list = null;
		if (_ComponentMap.containsKey(type) == false) {
			list = new HashMap<Integer, ComponentBase>();
			_ComponentMap.put(type, list);
		} else {
			list = _ComponentMap.get(type);
		}
		list.put(component.getId(), component);

		_Configuration.set(EngineInfo.ComponentIndexMap.get(type));

		_Ng.EntityBuilder.treeAddObject(this);

		return (T) component;
	}

	public <T extends ComponentBase> List<T> getComponents(Class<T> type) {
		Map<Integer, ComponentBase> componentMap = _ComponentMap.get(type);
		if (componentMap == null)
			return emptyList;
		return new ArrayList<T>((Collection<? extends T>) (componentMap.values()));
	}

	public <T extends ComponentBase> T getComponent(Class<T> type) {
		Map<Integer, ComponentBase> componentMap = _ComponentMap.get(type);
		if (componentMap == null)
			return null;
		return (T) componentMap.entrySet().iterator().next().getValue();
	}

	public <T extends ComponentBase> T getComponent(Class<T> type, int id) {
		Map<Integer, ComponentBase> componentMap = _ComponentMap.get(type);
		if (componentMap == null)
			return null;
		return (T) componentMap.get(id);
	}

	public <T extends ComponentBase> void removeComponent(Class<T> type, int id) {
		_Ng.EntityBuilder.treeRemoveObject(this);

		Map<Integer, ComponentBase> componentMap = _ComponentMap.get(type);
		if (componentMap == null) {
			_Ng.EntityBuilder.treeAddObject(this);
			return;
		}
		ComponentBase component = componentMap.remove(id);
		_ComponentFactory.removeComponent(type, component);

		if (componentMap.size() == 0) {
			_Configuration.clear(EngineInfo.ComponentIndexMap.get(type));
			_ComponentMap.remove(type);
		}

		_Ng.EntityBuilder.treeAddObject(this);
	}

	public <T extends ComponentBase> void removeComponent(Class<T> type) {
		_Ng.EntityBuilder.treeRemoveObject(this);

		Map<Integer, ComponentBase> components = _ComponentMap.get(type);
		if (components == null) {
			_Ng.EntityBuilder.treeAddObject(this);
			return;
		}
		for (Map.Entry<Integer, ComponentBase> iter : components.entrySet()) {
			_ComponentFactory.removeComponent(type, iter.getValue());
		}
		_ComponentMap.get(type).clear();
		_ComponentMap.remove(type);

		_Configuration.clear(EngineInfo.ComponentIndexMap.get(type));

		_Ng.EntityBuilder.treeAddObject(this);
	}

	public Entity clearComponents() {
		for (Map.Entry<Class<?>, Map<Integer, ComponentBase>> typeMap : _ComponentMap.entrySet()) {
			Map<Integer, ComponentBase> components = typeMap.getValue();
			Class<?> type = typeMap.getKey();
			for (Map.Entry<Integer, ComponentBase> iter : components.entrySet()) {
				_ComponentFactory.removeComponent(type, iter.getValue());
			}
		}
		_ComponentMap.clear();
		_Configuration.clear();
		return this;
	}

	public <T extends ComponentBase> boolean hasComponent(Class<T> type) {
		return _ComponentMap.containsKey(type);
	}

	public int getId() {
		return Id;
	}

	public boolean hasParent() {
		return Parent != null;
	}

	public Entity getParent() {
		return Parent;
	}
	
	public Set<Entity> getChildren(){
		return _Children;
	}

	public Entity setParent(Entity ent) {
		if (!canParent(ent))
			return this;
		Parent = ent;
		_ParentName = ent.Name;
		Parent._Children.add(this);
		return this;
	}

	public Entity setParent(String name) {
		Entity ent = _Ng.EntityBuilder.getByName(name);
		return setParent(ent);
	}
	
	public boolean canParent(Entity e){
		if(e == this || e == null){
			return false;
		}
		if(Parent == null){
			return true;
		}
		return Parent.canParent(e);
	}
	
	public Entity getLastParent(){
		if(Parent == null){
			return this;
		}
		return Parent.getLastParent();
	}

	public Entity detachParent() {
		Parent = null;
		_ParentName = "";
		Parent._Children.remove(this);
		return this;
	}

	protected void Save(XmlWriter element, XmlComponent _XmlComponent) throws Exception {
		element.element("Entity").attribute("Name", Name).attribute("Parent", _ParentName);
		for (Map.Entry<Class<?>, Map<Integer, ComponentBase>> ComponentsIndexMap : _ComponentMap.entrySet()) {
			for (Map.Entry<Integer, ComponentBase> Components : ComponentsIndexMap.getValue().entrySet()) {
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
		_ParentName = element.get("Parent");
		for (Element el : element.getChildrenByName("Component")) {
			String type = el.get("_Type");

			Class<?> cls = Class.forName(type);

			_XmlComponent.Load(addComponentUnsafe(cls), el);
		}
	}

	public Entity setName(String name) {
		if (name.length() == 0) {
			name = "~";
		}
		_Ng.EntityBuilder.setEntityName(this.Name, name);
		return this;
	}

	public String getName() {
		return Name;
	}

	protected List<ComponentBase> getComponents() {
		List<ComponentBase> ret = new ArrayList<ComponentBase>();
		for (Entry<Class<?>, Map<Integer, ComponentBase>> componentMap : _ComponentMap.entrySet()) {
			ret.addAll(componentMap.getValue().values());
		}
		return ret;
	}
}
