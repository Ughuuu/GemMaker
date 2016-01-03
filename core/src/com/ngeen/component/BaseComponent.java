package com.ngeen.component;

import com.ngeen.engine.Ngeen;

/**
 * The base for all Components. Has to be implemented.
 * @author Dragos
 *
 */
public abstract class BaseComponent {	
	/**
	 * The unique id of the current component. First is guaranteed to be 0.
	 */
	protected int Id;	
	/**
	 * Variable holds if the component is active or not.
	 */	
	protected boolean Enable = true;
	private static int _UniqueId = 0;
	
	/**
	 * Create a BaseComponent with an unique id.
	 */
	public BaseComponent() {
		Id = _UniqueId++;
	}	
	
	/**
	 * Get the id of the object.
	 */
	public final int getId(){
		return Id;
	}
	
	/**
	 * Get if the current object is enabled or not.
	 * @return The state of the variable Enable.
	 */
	public final boolean getEnabled(){
		return Enable;
	}
	
	/**
	 * Set this component to enabled or disabled.
	 * @param Enable The state of working of the component.
	 */
	public final void setEnabled(boolean Enable){
		this.Enable = Enable;
	}
}
