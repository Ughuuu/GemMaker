package com.ngeen.entity;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.ngeen.component.ComponentFactory;
import com.ngeen.debug.Debugger;
import com.ngeen.engine.EngineInfo;
import com.ngeen.engine.Ngeen;
import com.ngeen.systems.SystemConfiguration;

public class EntityFactory {
	private Map<Integer, Entity> _EntityMap;
	private Map<String, Integer> _EntityNameMap;

	private Map<Comparator, Map<Set<Class<?>>, Set<Entity>>> _EntityTrees;

	private Entity[] _EntityCache;
	private int _EntityCacheIndex = 0;
	private final Ngeen _Ng;
	private final ComponentFactory _ComponentFactory;

	public EntityFactory(Ngeen ng, ComponentFactory _ComponentFactory) {
		_Ng = ng;
		this._ComponentFactory = _ComponentFactory;
		_EntityCache = new Entity[EngineInfo.EntitiesCache];
		_EntityMap = new HashMap<Integer, Entity>();
		_EntityNameMap = new HashMap<String, Integer>();
		_EntityTrees = new HashMap<Comparator, Map<Set<Class<?>>, Set<Entity>>>();
	}

	public Entity makeEntity(String name){
		if(_EntityNameMap.containsKey(name)==true){
			Debugger.log("Object already exists.");
			return null;
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
		return ret;
	}

	private <T> List<Set<T>> powerSet(Set<T> originalSet) {
		int resultSize = (int) Math.pow(2, originalSet.size());
		List<Set<T>> resultPowerSet = new ArrayList<Set<T>>(resultSize);

		resultPowerSet.add(new TreeSet<T>());

		for (T itemFromOriginalSet : originalSet) {
			int startingResultSize = resultPowerSet.size();
			for (int i = 0; i < startingResultSize; i++) {
				Set<T> newSubset = new TreeSet<T>(resultPowerSet.get(i));
				newSubset.add(itemFromOriginalSet);

				resultPowerSet.add(newSubset);
			}
		}
		return resultPowerSet;
	}

	protected void treeRemoveObject(Entity e){
		for(Map.Entry<Comparator, Map<Set<Class<?>>, Set<Entity>>> EntityTreePair : _EntityTrees.entrySet()){
			Map<Set<Class<?>>, Set<Entity>> EntityTree = EntityTreePair.getValue();
			Set<Class<?>> typeArray = e.getComponentTypes();
			List<Set<Class<?>>> powerSetList = powerSet(typeArray); 
			for(Set<Class<?>> config : powerSetList){
				if(EntityTree.containsKey(config)){
					if(EntityTree.get(config).contains(e)){
						EntityTree.get(config).remove(e);
					}
				}
			}
			EntityTree.remove(e);
		}
	}

	protected void treeAddObject(Entity e){
		for(Map.Entry<Comparator, Map<Set<Class<?>>, Set<Entity>>> EntityTreePair : _EntityTrees.entrySet()){
			Map<Set<Class<?>>, Set<Entity>> EntityTree = EntityTreePair.getValue();
			Set<Class<?>> typeArray = e.getComponentTypes();
			List<Set<Class<?>>> powerSetList = powerSet(typeArray); 
			for(Set<Class<?>> config : powerSetList){
				if(EntityTree.containsKey(config)){
					if(EntityTree.get(config).contains(e)){
						EntityTree.get(config).add(e);
					}
				}
			}
			EntityTree.remove(e);
		}
	}

	public void removeEntity(int index) {
		Entity ent = _EntityMap.remove(index);
		cacheComponents(ent);
		_EntityNameMap.remove(ent.Name);
		if (_EntityCacheIndex < EngineInfo.EntitiesCache) {
			_EntityCache[_EntityCacheIndex] = ent;
			_EntityCacheIndex++;
		}
	}
	
	public void removeEntity(String name) {
		Integer id = _EntityNameMap.remove(name);
		Entity ent = _EntityMap.remove(id);
		cacheComponents(ent);
		if (_EntityCacheIndex < EngineInfo.EntitiesCache) {
			_EntityCache[_EntityCacheIndex] = ent;
			_EntityCacheIndex++;
		}
	}

	public void removeEntity(Entity ent) {
		_EntityMap.remove(ent.Id);
		_EntityNameMap.remove(ent.Name);
		cacheComponents(ent);
		if (_EntityCacheIndex < EngineInfo.EntitiesCache) {
			_EntityCache[_EntityCacheIndex] = ent;
			_EntityCacheIndex++;
		}
	}

	private void cacheComponents(Entity ent) {
		ent.clearComponents();
	}

	/**
	 * Get entity by name.
	 * 
	 * @param tag
	 *            The name of the object.
	 * @return
	 */
	public Entity getByName(String tag) {
		Integer id = _EntityNameMap.get(tag);
		if(id == null)
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
	
	public void clear(){
		_EntityMap.clear();
		_EntityNameMap.clear();

		_EntityTrees.clear();

		_EntityCacheIndex = 0;
	}
}
