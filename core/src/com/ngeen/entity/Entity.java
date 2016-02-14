package com.ngeen.entity;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.component.ComponentBase;
import com.ngeen.component.ComponentFactory;
import com.ngeen.component.XmlComponent;
import com.ngeen.component.ui.ComponentUIBase;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.EngineInfo;
import com.ngeen.engine.Ngeen;

/**
 * @composed 1 has * ComponentFactory
 */
public class Entity {
	private static int _Unique_id = 0;
	private static final List emptyList = new ArrayList();
	private List<Entity> _Children;
	private final ComponentFactory _ComponentFactory;
	private Map<Class<?>, Map<Integer, ComponentBase>> _ComponentMap;
	private BitSet _Configuration = new BitSet(EngineInfo.TotalComponents);
	private int _Order = -1;
	protected final Ngeen _Ng;
	protected String _ParentName = "null";
	protected int Id;
	protected String Name;

	protected Entity Parent;

	public Entity(Ngeen ng, ComponentFactory componentBuilder, String name) {
		_Ng = ng;
		_ComponentFactory = componentBuilder;
		this.Name = name;
		Id = _Unique_id++;
		_ComponentMap = new HashMap<Class<?>, Map<Integer, ComponentBase>>();
		_Children = new ArrayList<Entity>();
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

	protected <T extends ComponentBase> T addSuperComponent(ComponentBase component) {
		Class<?> type = component.getClass().getSuperclass();
		_Ng.EntityBuilder.treeRemoveObject(this);

		if (_ComponentMap.containsKey(type)) {
			_Ng.EntityBuilder.treeAddObject(this);
			return (T) _ComponentMap.get(type).entrySet().iterator().next().getValue();
		}

		_ComponentFactory.insertSuperComponent(component);

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

	public boolean canParent(Entity e) {
		if (e == this || e == null) {
			return false;
		}
		if (Parent == null) {
			return true;
		}
		return Parent.canParent(e);
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

	public Entity detachParent() {
		if (Parent != null) {
			Parent._Children.remove(this);
		}
		_ParentName = "";
		_Order = -1;
		if (Parent != null)
			Parent.recountChildren();
		Entity spare = Parent;
		Parent = null;
		_Ng.EntityBuilder.Parented(this, spare);
		return this;
	}

	public List<Entity> getChildren() {
		return _Children;
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

	public <T extends ComponentBase> List<T> getComponents(Class<T> type) {
		Map<Integer, ComponentBase> componentMap = _ComponentMap.get(type);
		if (componentMap == null)
			return emptyList;
		return new ArrayList<T>((Collection<? extends T>) (componentMap.values()));
	}

	public Set<Class<?>> getComponentTypes() {
		return _ComponentMap.keySet();
	}

	public BitSet getConfiguration() {
		return _Configuration;
	}

	public int getId() {
		return Id;
	}

	public Entity getLastParent() {
		Entity ent = getLastParentInternal();
		if (ent == this) {
			return null;
		}
		return ent;
	}

	public String getName() {
		return Name;
	}

	public int getOrder() {
		return _Order;
	}

	public Entity getParent() {
		return Parent;
	}

	public <T extends ComponentBase> boolean hasComponent(Class<T> type) {
		return _ComponentMap.containsKey(type);
	}

	public boolean hasParent() {
		return Parent != null;
	}

	public void remove() {
		detachParent();
		_Ng.EntityBuilder.removeEntity(this);
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

	public Entity setName(String name) {
		if (name.length() == 0) {
			name = "~";
		}
		_Ng.EntityBuilder.setEntityName(this.Name, name);
		List<ComponentBase> comps = getComponents();
		for(ComponentBase comp : comps){
			if(comp instanceof ComponentUIBase){
				_ComponentFactory.changeName((ComponentUIBase)comp, name);
			}
		}
		return this;
	}

	public Entity setParent(Entity ent) {
		if (!canParent(ent))
			return this;
		Parent = ent;
		_ParentName = ent.Name;
		Parent._Children.add(this);
		this._Order = Parent._Children.size() - 1;
		_Ng.EntityBuilder.Parented(this, ent);
		return this;
	}

	public Entity setParent(String name) {
		Entity ent = _Ng.EntityBuilder.getByName(name);
		return setParent(ent);
	}

	private ComponentBase addComponentUnsafe(Class<?> type) {
		_Ng.EntityBuilder.treeRemoveObject(this);

		if (_ComponentMap.containsKey(type)) {
			_Ng.EntityBuilder.treeAddObject(this);
			return _ComponentMap.get(type).entrySet().iterator().next().getValue();
		}

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

	private Entity getLastParentInternal() {
		if (Parent == null) {
			return this;
		}
		return Parent.getLastParentInternal();
	}

	private void recountChildren() {
		int prev = -1;
		for (int i = 0, j = 0; i < _Children.size(); i++, j++) {
			Entity ent = _Children.get(i);
			int act = ent._Order;
			if (act - prev != -1) {
				ent._Order--;
			}
			prev = act;
		}
	}

	protected List<ComponentBase> getComponents() {
		List<ComponentBase> ret = new ArrayList<ComponentBase>();
		for (Entry<Class<?>, Map<Integer, ComponentBase>> componentMap : _ComponentMap.entrySet()) {
			ret.addAll(componentMap.getValue().values());
		}
		return ret;
	}

	protected void Load(Element element, XmlComponent _XmlComponent) throws Exception {
		_ParentName = element.get("Parent");
		_Order = element.getInt("_Order");
		for (Element el : element.getChildrenByName("Component")) {
			try {
				String type = el.get("_Type");

				Class<?> cls = Class.forName(type);

				_XmlComponent.Load(addComponentUnsafe(cls), el);
			} catch (Exception exp) {
				Debugger.log(exp);
			}
		}
	}

	protected void reset(String name) {
		Id = _Unique_id++;
		this.Name = name;
	}

	protected void Save(XmlWriter element, XmlComponent _XmlComponent) throws Exception {
		element.element("Entity").attribute("Name", Name).attribute("Parent", _ParentName).attribute("_Order", _Order);
		for (Map.Entry<Class<?>, Map<Integer, ComponentBase>> ComponentsIndexMap : _ComponentMap.entrySet()) {
			for (Map.Entry<Integer, ComponentBase> Components : ComponentsIndexMap.getValue().entrySet()) {
				_XmlComponent.Save(Components.getValue(), element);
			}
		}
		element.pop();
	}

	protected void setOrder(int newOrder) {
		//if the order is right, do nothing. Reduces number of operations.
		if(_Order == newOrder)
			return;
		Collections.swap(_Children, _Order, newOrder);
		_Ng.EntityBuilder.Order(this, _Children.get(_Order));
		_Children.get(_Order)._Order = newOrder;
		_Children.get(newOrder)._Order = _Order;
	}
}
