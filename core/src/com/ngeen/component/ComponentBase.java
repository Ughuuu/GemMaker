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
	protected Class<?> _Type;
	protected Class<?> _SubType;
	/**
	 * Variable holds if the component is active or not.
	 */
	protected boolean Enable = true;
	private static int _UniqueId = 0;
	protected final Ngeen _Ng;
	protected Entity _Owner;
	protected boolean Saved = false;
	
	/**
	 * Create a BaseComponent with an unique id.
	 */
	public ComponentBase(Ngeen ng, Entity ent) {
		_Ng = ng;
		_Type = this.getClass();
		_SubType = this.getClass().getSuperclass();
		_Owner = ent;
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
	public <T extends ComponentBase> T setEnabled(boolean Enable) {
		this.Enable = Enable;
		return (T) this;
	}
	
	protected <T extends ComponentBase> T setOwner(Entity ent){
		_Owner = ent;
		return (T) this;
	}
	
	public <T extends ComponentBase> T remove(){
		_Owner.removeComponent(this.getClass(), Id);
		return (T) this;
	}
	
	public Entity getOwner(){
		return _Owner;
	}
	
	public Class<?> getType(){
		return _Type;
	}
	
	public Class<?> getSubType(){
		return _SubType;
	}
	
	protected abstract void Save(XmlWriter element) throws Exception;
	
	protected abstract void Load(XmlReader.Element element) throws Exception;
}
