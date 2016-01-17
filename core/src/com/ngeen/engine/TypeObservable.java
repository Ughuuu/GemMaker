package com.ngeen.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ngeen.component.ComponentBase;
import com.ngeen.entity.Entity;

public class TypeObservable<T> {
	protected List<TypeObserver> _Observers = new ArrayList<TypeObserver>();
	
	protected void NotifyRemove(ComponentBase obj){
		for(TypeObserver observer : _Observers){
			observer.Removed(obj);
		}
	}
	
	protected void NotifyAdd(ComponentBase obj){
		for(TypeObserver observer : _Observers){
			observer.Added(obj);
		}
	}
	
	protected void NotifyChange(ComponentBase obj){
		for(TypeObserver observer : _Observers){
			observer.ChangedComponent(obj);
		}
	}
	
	public void addObserver(TypeObserver... entityObservers){
		_Observers.addAll(Arrays.asList(entityObservers));
	}
	
	protected void NotifyParented(Entity obj){
		for(TypeObserver observer : _Observers){
			observer.Parented(obj);
		}
	}
}
