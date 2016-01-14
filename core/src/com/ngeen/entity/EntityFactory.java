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
import java.util.function.Consumer;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.ngeen.component.ComponentBase;
import com.ngeen.component.ComponentFactory;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.EngineInfo;
import com.ngeen.engine.Ngeen;
import com.ngeen.engine.SystemFactory;
import com.ngeen.engine.TypeObservable;
import com.ngeen.engine.TypeObserver;
import com.ngeen.systems.SystemBase;
import com.ngeen.systems.SystemConfiguration;

public class EntityFactory extends TypeObservable<Entity>{
	private Map<Integer, Entity> _EntityMap;
	private Map<String, Integer> _EntityNameMap;

	private Map<Comparator<Entity>, Map<BitSet, Set<Entity>>> _EntityTrees;
	private final SystemFactory _SystemBuilder;

	private Entity[] _EntityCache;
	private int _EntityCacheIndex = 0;
	private final Ngeen _Ng;
	private final ComponentFactory _ComponentFactory;
	private final Set<Entity> _EmptySet;
	
	public EntityFactory(Ngeen ng, ComponentFactory _ComponentFactory, SystemFactory _SystemBuilder) {
		_Ng = ng;
		this._SystemBuilder = _SystemBuilder;
		this._ComponentFactory = _ComponentFactory;
		_EntityCache = new Entity[EngineInfo.EntitiesCache];
		_EntityMap = new HashMap<Integer, Entity>();
		_EntityNameMap = new HashMap<String, Integer>();
		_EntityTrees = new HashMap<Comparator<Entity>, Map<BitSet, Set<Entity>>>();
		_EmptySet = new TreeSet<Entity>();
	}

	public Entity makeEntity(String name) {
		if (_EntityNameMap.containsKey(name) == true) {
			Debugger.log("Object already exists " + name + ".");
			return _EntityMap.get((_EntityNameMap.get(name)));
		}
		Entity ret;
		if (_EntityCacheIndex != 0) {// reuse deleted entities
			_EntityCacheIndex--;
			ret = _EntityCache[_EntityCacheIndex];
			ret.reset(name);
		} else {
			ret = new Entity(_Ng, _ComponentFactory, name);
		}
		_EntityMap.put(ret.Id, ret);
		_EntityNameMap.put(ret.Name, ret.Id);
		NotifyAdd(ret);
		return ret;
	}

	/**
	 * Not yet implemented.
	 * @param ent
	 * @return
	 */
	public Entity makeEntity(Entity ent) {
		String name = ent.Name + "1";
		if (_EntityNameMap.containsKey(name) == true) {
			Debugger.log("Object already exists " + name + ".");
			return _EntityMap.get((_EntityNameMap.get(name)));
		}
		Entity ret;
		if (_EntityCacheIndex != 0) {// reuse deleted entities
			_EntityCacheIndex--;
			ret = _EntityCache[_EntityCacheIndex];
			ret.reset(name);
		} else {
			ret = new Entity(_Ng, _ComponentFactory, name);
		}
		_EntityMap.put(ret.Id, ret);
		_EntityNameMap.put(ret.Name, ret.Id);
		for (ComponentBase comp : ent.getComponents()) {

		}
		return ret;
	}

	public void setEntityName(String oldName, String newName) {
		if (_EntityNameMap.containsKey(newName) == true) {
			return;
		}
		Integer id = _EntityNameMap.remove(oldName);
		Entity ent = _EntityMap.get(id);
		ent.Name = newName;
		_EntityNameMap.put(newName, id);
	}

	protected void treeRemoveObject(Entity e) {
		for (Entry<Comparator<Entity>, Map<BitSet, Set<Entity>>> EntityTreePair : _EntityTrees.entrySet()) {
			Map<BitSet, Set<Entity>> EntityTree = EntityTreePair.getValue();

			for (Map.Entry<BitSet, Set<Entity>> entitiesWithClass : EntityTree.entrySet()) {
				BitSet typeArray = (BitSet) e.getConfiguration().clone();
				BitSet entities = (BitSet) entitiesWithClass.getKey();

				typeArray.and(entities);

				if (entities.equals(typeArray)) {
					entitiesWithClass.getValue().remove(e);
				}

			}
		}
	}

	protected void treeAddObject(Entity e) {
		for (Entry<Comparator<Entity>, Map<BitSet, Set<Entity>>> EntityTreePair : _EntityTrees.entrySet()) {
			Map<BitSet, Set<Entity>> EntityTree = EntityTreePair.getValue();

			for (Map.Entry<BitSet, Set<Entity>> entitiesWithClass : EntityTree.entrySet()) {
				BitSet typeArray = (BitSet) e.getConfiguration().clone();
				BitSet entities = (BitSet) entitiesWithClass.getKey();

				typeArray.and(entities);

				if (entities.equals(typeArray)) {
					entitiesWithClass.getValue().add(e);
				}
			}
		}
	}

	public void removeEntity(int index) {
		Entity ent = _EntityMap.remove(index);
		if(ent == null)
			return;
		treeRemoveObject(ent);
		cacheComponents(ent);
		_EntityNameMap.remove(ent.Name);
		if (_EntityCacheIndex < EngineInfo.EntitiesCache) {
			_EntityCache[_EntityCacheIndex] = ent;
			_EntityCacheIndex++;
		}
		NotifyRemove(ent);
	}

	public void removeEntity(String name) {
		Integer id = _EntityNameMap.remove(name);
		Entity ent = _EntityMap.remove(id);
		if(ent == null)
			return;
		treeRemoveObject(ent);
		cacheComponents(ent);
		if (_EntityCacheIndex < EngineInfo.EntitiesCache) {
			_EntityCache[_EntityCacheIndex] = ent;
			_EntityCacheIndex++;
		}
		NotifyRemove(ent);
	}

	public void removeEntity(Entity ent) {
		if(ent == null)
			return;
		treeRemoveObject(ent);
		_EntityMap.remove(ent.Id);
		_EntityNameMap.remove(ent.Name);
		cacheComponents(ent);
		if (_EntityCacheIndex < EngineInfo.EntitiesCache) {
			_EntityCache[_EntityCacheIndex] = ent;
			_EntityCacheIndex++;
		}
		NotifyRemove(ent);
	}

	private void cacheComponents(Entity ent) {
		ent.clearComponents();
	}

	/**
	 * Get entity by name.
	 * 
	 * @param tag The name of the object.
	 * @return
	 */
	public Entity getByName(String tag) {
		Integer id = _EntityNameMap.get(tag);
		if (id == null)
			return null;
		return _EntityMap.get(id);
	}

	/**
	 * Get entity by it's id.
	 * 
	 * @param id
	 * @return
	 */
	public Entity getById(int id) {
		return _EntityMap.get(id);
	}

	public void clear() {
		for(Entry<Integer, Entity> entity:_EntityMap.entrySet()){
			NotifyRemove((entity.getValue()));
		}
		_EntityMap.clear();
		_EntityNameMap.clear();

		for (Map.Entry<Comparator<Entity>, Map<BitSet, Set<Entity>>> entry : _EntityTrees.entrySet()) {
			for (Entry<BitSet, Set<Entity>> entities : entry.getValue().entrySet()) {
				entities.getValue().clear();
			}
		}
		//_EntityCacheIndex = 0;
	}

	public void addSystem(SystemBase system) {
		Comparator<Entity> comp = system.getComparator();
		SystemConfiguration config = system.getConfiguration();
		if (!_EntityTrees.containsKey(comp)) {
			_EntityTrees.put(comp, new HashMap<BitSet, Set<Entity>>());
		}
		for (BitSet bits : config.getConfiguration()) {
			if (!_EntityTrees.get(comp).containsKey(bits)) {
				_EntityTrees.get(comp).put(bits, new TreeSet<Entity>(comp));
			}
		}
	}

	public Set<Entity> getEntitiesForSystem(SystemBase system) {
		Comparator<Entity> comp = system.getComparator();
		SystemConfiguration config = system.getConfiguration();
		List<BitSet> configs = config.getConfiguration();
		if (configs == null) {
			return _EmptySet;
		}
		Set<Entity> entSet = new TreeSet<Entity>(comp);

		for (BitSet configuration : configs) {
			Set<Entity> currentSet = _EntityTrees.get(comp).get(configuration);
			entSet.addAll(currentSet);
		}
		return entSet;
	}

	public List<Entity> getEntities() {
		return new ArrayList(_EntityMap.values());
	}
}
