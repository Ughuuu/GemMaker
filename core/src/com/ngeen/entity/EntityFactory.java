package com.ngeen.entity;

import java.util.HashMap;
import java.util.Map;

import com.ngeen.engine.Constant;
import com.ngeen.engine.Ngeen;

public class EntityFactory {
	private Map<Integer, Entity> _EntityMap;
	private Map<String, Integer> _EntityNameMap;
	// private
	private Entity[] _EntityCache;
	private int _EntityCacheIndex = 0;
	private final Ngeen _Ng;

	public EntityFactory(Ngeen ng) {
		_Ng = ng;
		_EntityCache = new Entity[Constant.ENTITIES_CACHE];
		_EntityMap = new HashMap<Integer, Entity>();
		_EntityNameMap = new HashMap<String, Integer>();
	}

	public Entity makeEntity(String name) {
		Entity ret;
		if (_EntityCacheIndex != 0) {// reuse deleted entities
			ret = _EntityCache[_EntityCacheIndex];
			_EntityCacheIndex--;
		} else {
			ret = new Entity(_Ng, name);
		}
		_EntityMap.put(ret.id, ret);
		_EntityNameMap.put(ret.name, ret.id);
		return ret;
	}

	public void removeEntityById(int index) {
		Entity ent = _EntityMap.remove(index);
		cacheComponents(ent);
		_EntityNameMap.remove(ent.name);
		if (_EntityCacheIndex < Constant.ENTITIES_CACHE) {
			_EntityCache[_EntityCacheIndex] = ent;
			_EntityCacheIndex++;
		}
	}

	public void removeEntity(Entity ent) {
		_EntityMap.remove(ent.id);
		_EntityNameMap.remove(ent.name);
		cacheComponents(ent);
		if (_EntityCacheIndex < Constant.ENTITIES_CACHE) {
			_EntityCache[_EntityCacheIndex] = ent;
			_EntityCacheIndex++;
		}
	}
	
	private void cacheComponents(Entity ent){
		_Ng.ComponentBuilder.removeComponent(type, component);
	}

	/**
	 * Get entity by name.
	 * 
	 * @param tag
	 *            The name of the object.
	 * @return
	 */
	public Entity getByName(String tag) {
		return _EntityMap.get(_EntityNameMap.get(tag));
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
}
