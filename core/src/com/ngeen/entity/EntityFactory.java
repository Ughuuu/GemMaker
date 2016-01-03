package com.ngeen.entity;

import java.util.List;
import java.util.Map;

import com.ngeen.engine.Constant;

public class EntityFactory {
	public static EntityFactory factory = new EntityFactory();
	
	private Map<Integer, Entity> entityMap;
	private Entity[] entityCache;
	private int entityCacheIndex = 0;
	
	public EntityFactory(){
		entityCache = new Entity[Constant.ENTITIES_CACHE];
	}
	
	public Entity makeEntity(){
		if(entityCacheIndex!=0){//reuse deleted entities
			Entity ret = entityCache[entityCacheIndex];
			entityCacheIndex--;
			return ret;
		}
		return new Entity();
	}
	
	public void removeEntityById(int index){
		Entity ent = entityMap.remove(index);
		if(entityCacheIndex<Constant.ENTITIES_CACHE){
			entityCache[entityCacheIndex] = ent;
			entityCacheIndex++;
		}
	}
	
	public void removeEntity(Entity ent){
		entityMap.remove(ent.);
		if(entityCacheIndex<Constant.ENTITIES_CACHE){
			entityCache[entityCacheIndex] = ent;
			entityCacheIndex++;
		}
	}
}
