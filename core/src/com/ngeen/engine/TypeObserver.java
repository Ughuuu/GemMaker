package com.ngeen.engine;

public interface TypeObserver<T> {
	
	public void Removed(T obj);
	
	public void Added(T obj);
	
	public void Changed(T obj);
}
