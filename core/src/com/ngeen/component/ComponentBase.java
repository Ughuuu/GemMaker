package com.ngeen.component;

import java.io.IOException;

import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlWriter;
import com.ngeen.engine.Ngeen;
import com.ngeen.entity.Entity;

/**
 * The base for all Components. Has to be implemented.
 * 
 * @author Dragos
 *
 */
public abstract class ComponentBase {
	/**
	 * The unique id of the current component. First is guaranteed to be 0.
	 */
	protected int Id;
	/**
	 * Variable holds if the component is active or not.
	 */
	protected boolean Enable = true;
	private static int _UniqueId = 0;
	protected final Ngeen _Ng;
	protected int OwnerId = -1;

	/**
	 * Create a BaseComponent with an unique id.
	 */
	public ComponentBase(Ngeen ng) {
		_Ng = ng;
		Id = _UniqueId++;
	}

	/**
	 * Get the id of the object.
	 */
	public final int getId() {
		return Id;
	}

	/**
	 * Get if the current object is enabled or not.
	 * 
	 * @return The state of the variable Enable.
	 */
	public final boolean getEnabled() {
		return Enable;
	}

	/**
	 * Set this component to enabled or disabled.
	 * 
	 * @param Enable
	 *            The state of working of the component.
	 */
	public final void setEnabled(boolean Enable) {
		this.Enable = Enable;
	}
	
	public void setOwner(int ParentId){
		OwnerId = ParentId;
	}
	
	public void remove(){
		_Ng.EntityBuilder.getById(OwnerId).removeComponent(this.getClass(), Id);
	}
	
	public Entity getOwner(){
		return _Ng.EntityBuilder.getById(OwnerId);
	}
	
	protected abstract void Save(XmlWriter element) throws Exception;
	
	protected abstract void Load(XmlReader.Element element) throws Exception;
}
