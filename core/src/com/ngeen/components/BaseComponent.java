package com.ngeen.components;

import com.ngeen.engine.Ngeen;

public class BaseComponent {
	
	protected int id, parent = -1;
	public boolean enable = true;
	private static int unique_id = 0;

	/**
	 * Create a BaseComponent with a unique id.
	 */
	public BaseComponent() {
		id = unique_id++;
	}
	
	public int getId(){
		return id;
	}
	
	public int getParentId(){
		return parent;
	}
}
