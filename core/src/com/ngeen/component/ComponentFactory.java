package com.ngeen.component;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ngeen.debug.Debugger;
import com.ngeen.engine.Constant;
import com.ngeen.engine.Ngeen;

public class ComponentFactory {
	private final Ngeen _Ng;
	// list not array because can't instantiate array of generic:(
	private Map<Class<?>, List<ComponentBase>> _ComponentCache;
	private Map<Class<?>, Integer> _ComponentCacheIndex;

	public ComponentFactory(Ngeen ng) {
		_Ng = ng;
		_ComponentCache = new HashMap<Class<?>, List<ComponentBase>>();
	}

	public <T extends ComponentBase> T createComponent(Class<?> type){
		try {
			T el = null;
			Integer size = _ComponentCacheIndex.get(type);
			if(size != null){
				size--;
				 _ComponentCacheIndex.put(type, size);
				List<ComponentBase> list = _ComponentCache.get(type);
				el = (T) list.get(size);
			}
			else{
				el = (T) type.getConstructor(ComponentFactory.class).newInstance(this);
			}
			return el;
		} catch (Exception e) {
			Debugger.log(e.toString());
		}
		return null;
	}

	public <T extends ComponentBase> void removeComponent(Class<?> type, T component) {
		int size = _ComponentCacheIndex.get(type);
		List<ComponentBase> list = null;
		if (!_ComponentCache.containsKey(type)) {
			list = new ArrayList<ComponentBase>(Constant.COMPONENT_CACHE);
			_ComponentCacheIndex.put(type, 0);
			size = 0;
			_ComponentCache.put(type, list);
		}
		if (list.size() < Constant.COMPONENT_CACHE) {
			_ComponentCacheIndex.put(type, size + 1);
			list.set(size, component);
		}
		size++;
	}
}
