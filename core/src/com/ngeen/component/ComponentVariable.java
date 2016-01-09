package com.ngeen.component;

import com.ngeen.engine.Ngeen;

public class ComponentVariable<T> extends ComponentBase {
	public String name = null;
	public T var = null;
	
	public ComponentVariable(Ngeen ng) {
		super(ng);
	}
}
