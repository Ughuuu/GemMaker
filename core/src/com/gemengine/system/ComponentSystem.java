package com.gemengine.system;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.MarkerManager;

import com.badlogic.gdx.utils.TimeUtils;
import com.gemengine.component.Component;
import com.gemengine.entity.Entity;
import com.gemengine.listener.ComponentListener;
import com.gemengine.listener.EntityComponentListener;
import com.gemengine.listener.PriorityListener;
import com.gemengine.listener.ComponentListener.ComponentChangeType;
import com.gemengine.listener.ComponentUpdaterListener;
import com.gemengine.system.base.TimedSystem;
import com.gemengine.system.manager.SystemManager;
import com.google.inject.Inject;

import lombok.val;
import lombok.extern.log4j.Log4j2;

/**
 * The basic system that handles component creation, handling, querying and
 * destruction.
 * 
 * @author Dragos
 *
 */
@Log4j2
public class ComponentSystem extends TimedSystem {
	private final Map<Integer, Component> components = new HashMap<Integer, Component>();
	private final Map<Integer, Entity> componentToEntity = new HashMap<Integer, Entity>();
	private final Map<Integer, Map<String, List<Integer>>> entityToTypeToComponents = new HashMap<Integer, Map<String, List<Integer>>>();

	private final Map<Integer, String> componentToType = new HashMap<Integer, String>();
	private final Map<String, List<Integer>> typeToComponents = new HashMap<String, List<Integer>>();

	private final SystemManager systemManager;
	private final List<ComponentUpdaterListener> componentUpdaterSystems = new ArrayList<ComponentUpdaterListener>();
	private final List<ComponentListener> componentListeners = new ArrayList<ComponentListener>();
	private final Map<Integer, List<EntityComponentListener>> entityComponentListeners = new HashMap<Integer, List<EntityComponentListener>>();
	private final Map<Set<String>, Set<Entity>> configurationToEntities = new HashMap<Set<String>, Set<Entity>>();

	private boolean dirty = false;
	private final TimingSystem timingSystem;

	@Inject
	public ComponentSystem(SystemManager systemManager, TimingSystem timingSystem) {
		super(1600, true, 3);
		this.systemManager = systemManager;
		this.timingSystem = timingSystem;
	}

	/**
	 * Add a {@link ComponentListener} listener. This listens for all component
	 * changes of given type.
	 * 
	 * @param componentListener
	 */
	public void addComponentListener(ComponentListener componentListener) {
		componentListeners.add(componentListener);
		Collections.sort(componentListeners, PriorityListener.getComparator());
	}

	/**
	 * Add a {@link ComponentUpdaterSystem}. This receives all components of
	 * requested type.
	 * 
	 * @param componentUpdater
	 */
	public void addComponentUpdater(ComponentUpdaterListener componentUpdater) {
		componentUpdaterSystems.add(componentUpdater);
		Collections.sort(componentUpdaterSystems, PriorityListener.getComparator());
	}

	/**
	 * Add a {@link EntityComponentListener}. This listens for changes in
	 * components from the entity chosen.
	 * 
	 * @param entityComponentListener
	 */
	public void addEntityComponentListener(EntityComponentListener entityComponentListener) {
		Entity owner = entityComponentListener.getOwner();
		List<EntityComponentListener> listeners = entityComponentListeners.get(owner.getId());
		if (listeners == null) {
			listeners = new ArrayList<EntityComponentListener>();
			entityComponentListeners.put(owner.getId(), listeners);
		}
		listeners.add(entityComponentListener);
		Collections.sort(listeners, PriorityListener.getComparator());
	}

	/**
	 * Clear all the components from an entity.
	 * 
	 * @param ent
	 *            The entity to clear components from
	 */
	public void clear(Entity ent) {
		clear(ent.getId());
	}

	private void clear(int ownerId) {
		val entityToComponent = get(ownerId, Component.class);
		if (!componentListeners.isEmpty()) {
			for (val component : entityToComponent) {
				List<String> types = new ArrayList<String>();
				supertypes(component.getClass(), types);
				for (ComponentListener listener : componentListeners) {
					if (listener != component) {
						for (String type : types) {
							if (listener.getConfiguration().contains(type)) {
								listener.onChange(ComponentChangeType.DELETE, component);
								break;
							}
						}
					}
				}
			}
		}
		for (val component : entityToComponent) {
			int id = component.getId();
			removeFromTypeMap(id, component.getClass());
			componentToEntity.remove(id);
		}
		entityToTypeToComponents.remove(ownerId);
		dirty = true;
	}

	/**
	 * Create a component on a given entity.
	 * 
	 * @param ent
	 *            The entity to make the component on
	 * @param type
	 *            The type of the component
	 * @return The created component
	 */
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
		dirty = true;
		return component;
	}

	/**
	 * Get a component from an entity.
	 * 
	 * @param ent
	 *            The entity to get component from
	 * @param type
	 *            The type of the component
	 * @return The component or null
	 */
	@SuppressWarnings("unchecked")
	public <T extends Component> List<T> get(Entity ent, Class<T> type) {
		return get(ent.getId(), type);
	}

	private <T extends Component> List<T> get(int entityId, Class<T> type) {
		int ownerId = entityId;
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

	/**
	 * Get a component from an entity.
	 * 
	 * @param ent
	 *            The entity to get component from
	 * @param id
	 *            The id of the component
	 * @return The component or null
	 */
	@SuppressWarnings("unchecked")
	public <T extends Component> T get(Entity ent, int id) {
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

	/**
	 * Get the owner of a component
	 * 
	 * @param id
	 *            the id of the component
	 * @return The owner of this component
	 */
	public Entity getOwner(int id) {
		return componentToEntity.get(id);
	}

	/**
	 * Get the owner of a component
	 * 
	 * @param component
	 *            The component to get the owner from
	 * @return The owner of this component
	 */
	public <T extends Component> Entity getOwner(T component) {
		return componentToEntity.get(component.getId());
	}

	/**
	 * Used by components to notify events.
	 * 
	 * @param event
	 *            the event name
	 * @param component
	 *            the component initiating the event
	 */
	public void notifyFrom(String event, Component component) {
		if (componentListeners != null) {
			List<String> types = new ArrayList<String>();
			supertypes(component.getClass(), types);
			for (ComponentListener listener : componentListeners) {
				if (listener != component) {
					for (String type : types) {
						if (listener.getConfiguration().contains(type)) {
							listener.onNotify(event, component);
							break;
						}
					}
				}
			}
		}
		List<EntityComponentListener> listeners = entityComponentListeners.get(getOwner(component.getId()).getId());
		if (listeners != null) {
			for (EntityComponentListener listener : listeners) {
				if (listener != component) {
					listener.onNotify(event, component);
				}
			}
		}
	}

	@Override
	public void onInit() {
	}

	@Override
	public void onUpdate(float delta) {
		long startUpdate = TimeUtils.millis();
		for (val updater : componentUpdaterSystems) {
			long start = TimeUtils.millis();
			try {
				updater.onBeforeEntities();
			} catch (Throwable t) {
				log.warn(MarkerManager.getMarker("gem"), "Component System before update", t);
			}
			timingSystem.addTiming(updater.getClass().getName() + "#onBeforeEntities", TimeUtils.millis() - start,
					getInterval());
		}
		for (val updater : componentUpdaterSystems) {
			long start = TimeUtils.millis();
			val configuration = updater.getConfiguration();
			Set<Entity> entities = entitiesFromConfiguration(configuration);
			for (val entity : entities) {
				try {
					updater.onNext(entity);
				} catch (Throwable t) {
					log.warn(MarkerManager.getMarker("gem"), "Component System update", t);
				}
			}
			timingSystem.addTiming(updater.getClass().getName() + "#onNext", TimeUtils.millis() - start, getInterval());
		}
		for (val updater : componentUpdaterSystems) {
			long start = TimeUtils.millis();
			try {
				updater.onAfterEntities();
			} catch (Throwable t) {
				log.warn(MarkerManager.getMarker("gem"), "Component System after update", t);
			}
			timingSystem.addTiming(updater.getClass().getName() + "#onBeforeEntities", TimeUtils.millis() - start,
					getInterval());
		}
		timingSystem.addTiming(getClass().getName() + "#onUpdate", TimeUtils.millis() - startUpdate, getInterval());
	}

	/**
	 * Remove the given component type from the given entity
	 * 
	 * @param ent
	 *            The entity to remove the component from
	 * @param type
	 *            The type of the component
	 */
	public <T extends Component> void remove(Entity ent, Class<T> type) {
		List<T> components = get(ent, type);
		for (T component : components) {
			remove(ent, component.getId());
		}
	}

	/**
	 * Remove the given component type from the given entity
	 * 
	 * @param ent
	 *            The entity to remove the component from
	 * @param id
	 *            The id of the component
	 */
	public void remove(Entity ent, int id) {
		if (!componentListeners.isEmpty()) {
			Component component = components.get(id);
			List<String> types = new ArrayList<String>();
			supertypes(component.getClass(), types);
			for (ComponentListener listener : componentListeners) {
				if (listener != component) {
					for (String type : types) {
						if (listener.getConfiguration().contains(type) && listener != component) {
							listener.onChange(ComponentChangeType.DELETE, component);
							break;
						}
					}
				}
			}
		}
		Component component = components.remove(id);
		if (component == null) {
			return;
		}
		removeFromTypeMap(id, component.getClass());
		removeFromEntityMap(ent, id, component.getClass());
		dirty = true;
	}

	/**
	 * Remove the given component type from the given entity
	 * 
	 * @param ent
	 *            The entity to remove the component from
	 * @param component
	 *            The component
	 */
	public <T extends Component> void remove(Entity ent, T component) {
		remove(ent, component.getId());
	}

	private <T extends Component> void addComponent(Entity ent, T component) {
		int id = component.getId();
		components.put(component.getId(), component);
		componentToEntity.put(component.getId(), ent);
		componentToType.put(id, component.getClass().getName());
		List<String> types = new ArrayList<String>();
		supertypes(component.getClass(), types);
		for (ComponentListener listener : componentListeners) {
			for (String type : types) {
				if (listener.getConfiguration().contains(type) && listener != component) {
					listener.onChange(ComponentChangeType.ADD, component);
					break;
				}
			}
		}
	}

	private <T extends Component> void addType(Entity ent, T component) {
		int ownerId = ent.getId();
		Class<?> componentClass = component.getClass();
		int componentId = component.getId();
		List<String> supertypes = new ArrayList<String>();
		supertypes(componentClass, supertypes);
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

	private Set<Entity> entitiesFromConfiguration(Set<String> configuration) {
		Set<Entity> entityCollection = configurationToEntities.get(configuration);
		if (entityCollection == null) {
			entityCollection = new HashSet<Entity>();
			configurationToEntities.put(configuration, entityCollection);
		}
		if (dirty) {
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
		}
		return entityCollection;
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
		supertypes(type, supertypes);
		for (String typeName : supertypes) {
			val typeToComponent = typeToComponents.get(typeName);
			if (typeToComponent.size() == 1) {
				typeToComponents.remove(typeName);
			} else {
				typeToComponent.remove(id);
			}
		}
	}

	private void supertypes(Class<?> cls, List<String> supertypes) {
		if (cls == null || cls.equals(Object.class)) {
			return;
		}
		supertypes.add(cls.getName());
		supertypes(cls.getSuperclass(), supertypes);
	}
}
