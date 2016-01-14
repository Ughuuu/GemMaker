package com.ngeen.engine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.ngeen.entity.Entity;

public class TypeObservable<T> {
	protected List<TypeObserver<T>> _Observers = new ArrayList<TypeObserver<T>>();
	
	protected void NotifyRemove(T obj){
		for(TypeObserver<T> observer : _Observers){
			observer.Removed(obj);
		}
	}
	
	protected void NotifyAdd(T obj){
		for(TypeObserver<T> observer : _Observers){
			observer.Added(obj);
		}
	}
	
	protected void NotifyChange(T obj){
		for(TypeObserver<T> observer : _Observers){
			observer.Changed(obj);
		}
	}
	
	public void addObserver(TypeObserver<T>... entityObservers){
		_Observers.addAll(Arrays.asList(entityObservers));
	}
}
