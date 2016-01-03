package com.ngeen.components;

import java.util.ArrayList;
import java.util.List;

import com.ngeen.engine.Ngeen;

public class BaseComponent {
	
	protected int id, parent = -1;
	protected List<Integer> child = new ArrayList<Integer>();
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
	
	public List<Integer> getChildrenId(){
		return child;
	}
	
	public Integer getChildId(){
		return child.get(0);
	}
}
