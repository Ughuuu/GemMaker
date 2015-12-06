package com.ngeen.components;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains ids of child objects that parent object has.
 * 
 * @author Dragos
 *
 */
public class ChildComponent extends BaseComponent {
	
	private List<Integer> child;

	/**
	 * Constructs a new ChildComponent.
	 */
	public ChildComponent() {
		super();
		child = new ArrayList<Integer>();
	}
	
	public List<Integer> getChildrenId(){
		return child;
	}
}
