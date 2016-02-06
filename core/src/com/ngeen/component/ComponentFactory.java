package com.ngeen.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ngeen.engine.EngineInfo;
import com.ngeen.engine.Ngeen;
import com.ngeen.engine.TypeObservable;
import com.ngeen.entity.Entity;

/**
 * <img src=
 * "https://raw.githubusercontent.com/Ughuuu/ngeen/online/core/doc/img/ComponentFactory.png"/>
 * 
 * @author Dragos
 * @opt shape node
 * @composed 1 has * ComponentBase
 */
public class ComponentFactory extends TypeObservable<ComponentBase> {
	// list not array because can't instantiate array of generic:(
	private Map<Class<?>, List<ComponentBase>> _ComponentCache;
	private Map<Class<?>, Integer> _ComponentCacheIndex;
	private final Ngeen _Ng;

	public ComponentFactory(Ngeen ng) {
		_Ng = ng;
		_ComponentCache = new HashMap<Class<?>, List<ComponentBase>>();
		_ComponentCacheIndex = new HashMap<Class<?>, Integer>();
	}

	public void clear() {
		_ComponentCache.clear();
		_ComponentCacheIndex.clear();
	}

	public <T extends ComponentBase> T createComponent(Class<?> type, Entity ent) {
		try {
			T el = null;
			Integer size = _ComponentCacheIndex.get(type);
			if (size != null && size > 0) {
				size--;
				List<ComponentBase> list = _ComponentCache.get(type);
				el = (T) list.get(size);
				_ComponentCacheIndex.put(type, size);
			} else {
				el = (T) type.getConstructor(Ngeen.class, Entity.class).newInstance(_Ng, ent);
			}
			NotifyAdd(el);
			return el;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void insertSuperComponent(ComponentBase component) {
		// don't track these, as they are already accounted for.
		// but do track them
		// NotifyAdd(component);
	}

	public <T extends ComponentBase> void removeComponent(Class<?> type, T component) {
		Integer size = _ComponentCacheIndex.get(type);
		List<ComponentBase> list = _ComponentCache.get(type);
		// construct the cache
		if (list == null) {
			list = new ArrayList<ComponentBase>(EngineInfo.ComponentCache);
			for (int i = 0; i < EngineInfo.ComponentCache; i++) {
				list.add(null);
			}
			_ComponentCache.put(type, list);
			size = 0;
			_ComponentCacheIndex.put(type, size);
			_ComponentCache.put(type, list);
		}
		// add in cache
		if (size < EngineInfo.ComponentCache) {
			_ComponentCacheIndex.put(type, size + 1);
			list.set(size, component);
		}
		size++;
		NotifyRemove(component);
	}
}
