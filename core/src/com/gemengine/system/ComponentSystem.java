package com.gemengine.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.logging.log4j.MarkerManager;

import com.gemengine.component.Component;
import com.gemengine.entity.Entity;
import com.gemengine.system.base.ComponentListener;
import com.gemengine.system.base.ComponentListener.ComponentChangeType;
import com.gemengine.system.base.EntityComponentListener;
import com.gemengine.system.base.ComponentUpdaterSystem;
import com.gemengine.system.base.TimedSystem;
import com.gemengine.system.manager.SystemManager;
import com.google.inject.Inject;

import lombok.val;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ComponentSystem extends TimedSystem {
	private final Map<Integer, Component> components = new HashMap<Integer, Component>();
	private final Map<Integer, Entity> componentToEntity = new HashMap<Integer, Entity>();
	private final Map<Integer, Map<String, List<Integer>>> entityToTypeToComponents = new HashMap<Integer, Map<String, List<Integer>>>();

	private final Map<Integer, String> componentToType = new HashMap<Integer, String>();
	private final Map<String, List<Integer>> typeToComponents = new HashMap<String, List<Integer>>();

	private final SystemManager systemManager;
	private final List<ComponentUpdaterSystem> componentUpdaterSystems = new ArrayList<ComponentUpdaterSystem>();
	private final List<ComponentListener> componentListeners = new ArrayList<ComponentListener>();
	private final List<EntityComponentListener> entityComponentListener = new ArrayList<EntityComponentListener>();
	private final Map<Set<String>, Set<Entity>> configurationToEntities = new HashMap<Set<String>, Set<Entity>>();

	@Inject
	public ComponentSystem(SystemManager systemManager) {
		super(1600, true, 3);
		this.systemManager = systemManager;
	}

	public void addComponentListener(ComponentListener componentListener) {
		componentListeners.add(componentListener);
	}

	public void addComponentUpdater(ComponentUpdaterSystem componentUpdater) {
		componentUpdaterSystems.add(componentUpdater);
		Collections.sort(componentUpdaterSystems);
	}

	public void notifyFrom(String event, Component component) {
		for (ComponentListener listener : componentListeners) {
			if (listener != component) {
				if (listener.getConfiguration().contains(component.getClass().getName())) {
					listener.onNotify(event, component);
				}
			}
		}
		for (EntityComponentListener listener : entityComponentListener) {
			if (listener.getOwner() == getOwner(component.getId()) && listener != component) {
				listener.onNotify(event, component);
			}
		}
	}

	public void clear(Entity ent) {
		clear(ent.getId());
	}

	public void clear(int ownerId) {
		val entityToComponent = entityToTypeToComponents.get(ownerId);
		for (val key : entityToComponent.entrySet()) {
			Component component = components.remove(key.getValue());
			for (ComponentListener listener : componentListeners) {
				if (listener.getConfiguration().contains(component.getClass().getName())) {
					listener.onChange(ComponentChangeType.DELETE, component);
				}
			}
			if (component == null) {
				continue;
			}
			int id = component.getId();
			removeFromTypeMap(id, component.getClass());
			componentToEntity.remove(id);
		}
		entityToTypeToComponents.remove(ownerId);
	}

	public <T extends Component> T create(Entity ent, Class<T> type) {
		if (ent == null) {
			return null;
		}
		Map<String, List<Integer>> types = entityToTypeToComponents.get(ent.getId());
		if (types != null) {
			List<Integer> componentFromType = types.get(type.getName());
			if ((componentFromType != null && !componentFromType.isEmpty())) {
				return null;
			}
		}
		T component = null;
		try {
			component = systemManager.inject(type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (component == null) {
			return null;
		}
		addType(ent, component);
		addComponent(ent, component);
		component.onCreate();
		return component;
	}

	@SuppressWarnings("unchecked")
	public <T extends Component> List<T> find(Entity ent, Class<T> type) {
		int ownerId = ent.getId();
		val typeToComponent = entityToTypeToComponents.get(ownerId);
		if (typeToComponent == null) {
			return null;
		}
		List<Integer> types = typeToComponent.get(type.getName());
		if (types == null) {
			return null;
		}
		List<T> entityComponents = new ArrayList<T>();
		for (int id : types) {
			entityComponents.add((T) components.get(id));
		}
		return entityComponents;
	}

	@SuppressWarnings("unchecked")
	public <T extends Component> T find(Entity ent, int id) {
		Entity owner = componentToEntity.get(id);
		if (owner == null || owner != ent) {
			return null;
		}
		Component component = components.get(id);
		if (component == null) {
			return null;
		}
		return (T) component;
	}

	public Entity getOwner(int id) {
		return componentToEntity.get(id);
	}

	@Override
	public void onInit() {
	}

	@Override
	public void onUpdate(float delta) {
		for (val updater : componentUpdaterSystems) {
			try {
				if (updater.isEnable()) {
					updater.onBeforeEntities();
				}
			} catch (Throwable t) {
				log.warn(MarkerManager.getMarker("gem"), "Component System before update", t);
				updater.setEnable(false);
			}
		}
		for (val updater : componentUpdaterSystems) {
			val configuration = updater.getConfiguration();
			val entities = getEntitiesFromConfiguration(configuration);
			for (val entity : entities) {
				try {
					if (updater.isEnable()) {
						updater.onNext(entity);
					}
				} catch (Throwable t) {
					log.warn(MarkerManager.getMarker("gem"), "Component System update", t);
					updater.setEnable(false);
				}
			}
		}
		for (val updater : componentUpdaterSystems) {
			try {
				if (updater.isEnable()) {
					updater.onAfterEntities();
				}
			} catch (Throwable t) {
				log.warn(MarkerManager.getMarker("gem"), "Component System after update", t);
				updater.setEnable(false);
			}
		}
	}

	public <T extends Component> void remove(Entity ent, Class<T> type) {
		List<T> components = find(ent, type);
		for (T component : components) {
			remove(ent, component.getId());
		}
	}

	public void remove(Entity ent, int id) {
		Component component = components.remove(id);
		if (component == null) {
			return;
		}
		for (ComponentListener listener : componentListeners) {
			if (listener.getConfiguration().contains(component.getClass().getName())) {
				listener.onChange(ComponentChangeType.DELETE, component);
			}
		}
		removeFromTypeMap(id, component.getClass());
		removeFromEntityMap(ent, id, component.getClass());
	}

	public <T extends Component> void remove(Entity ent, T component) {
		remove(ent, component.getId());
	}

	private <T extends Component> void addComponent(Entity ent, T component) {
		int id = component.getId();
		components.put(component.getId(), component);
		componentToEntity.put(component.getId(), ent);
		componentToType.put(id, component.getClass().getName());
		for (ComponentListener listener : componentListeners) {
			if (listener.getConfiguration().contains(component.getClass().getName())) {
				listener.onChange(ComponentChangeType.ADD, component);
			}
		}
	}

	private <T extends Component> void addType(Entity ent, T component) {
		int ownerId = ent.getId();
		Class<?> componentClass = component.getClass();
		int componentId = component.getId();
		List<String> supertypes = new ArrayList<String>();
		getSupertypes(componentClass, supertypes);
		Map<String, List<Integer>> typeToComponentLimited = entityToTypeToComponents.get(ownerId);
		if (typeToComponentLimited == null) {
			typeToComponentLimited = new HashMap<String, List<Integer>>();
			entityToTypeToComponents.put(ownerId, typeToComponentLimited);
		}
		for (String supertype : supertypes) {
			List<Integer> typeComponents = typeToComponentLimited.get(supertype);
			if (typeComponents == null) {
				typeComponents = new ArrayList<Integer>();
				typeToComponentLimited.put(supertype, typeComponents);
			}
			typeComponents.add(componentId);

			List<Integer> typeToComponent = typeToComponents.get(supertype);
			if (typeToComponent == null) {
				typeToComponent = new ArrayList<Integer>();
				typeToComponents.put(supertype, typeToComponent);
			}
			typeToComponent.add(componentId);
		}
	}

	private Set<Entity> getEntitiesFromConfiguration(Set<String> configuration) {
		Set<Entity> entityCollection = configurationToEntities.get(configuration);
		if (entityCollection == null) {
			entityCollection = new HashSet<Entity>();
			configurationToEntities.put(configuration, entityCollection);
		}
		entityCollection.clear();
		for (String type : configuration) {
			List<Integer> components = typeToComponents.get(type);
			if (components == null) {
				continue;
			}
			for (Integer component : components) {
				entityCollection.add(componentToEntity.get(component));
			}
		}
		return entityCollection;
	}

	private void getSupertypes(Class<?> cls, List<String> supertypes) {
		if (cls == null || cls.equals(Object.class)) {
			return;
		}
		supertypes.add(cls.getName());
		getSupertypes(cls.getSuperclass(), supertypes);
	}

	private void removeFromEntityMap(Entity ent, Integer id, Class<?> type) {
		int ownerId = ent.getId();
		val entityToComponent = entityToTypeToComponents.get(ownerId);
		entityToComponent.remove(type.getName());
		componentToEntity.remove(id);
	}

	private void removeFromTypeMap(Integer id, Class<?> type) {
		componentToType.remove(id);
		List<String> supertypes = new ArrayList<String>();
		getSupertypes(type, supertypes);
		for (String typeName : supertypes) {
			val typeToComponent = typeToComponents.get(typeName);
			if (typeToComponent.size() == 1) {
				typeToComponents.remove(typeName);
			} else {
				typeToComponent.remove(id);
			}
		}
	}
}
