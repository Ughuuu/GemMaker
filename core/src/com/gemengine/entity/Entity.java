package com.gemengine.entity;

import java.util.List;
import java.util.Set;

import org.apache.logging.log4j.MarkerManager;
import com.gemengine.component.Component;
import com.gemengine.system.ComponentSystem;
import com.gemengine.system.EntitySystem;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;

@Log4j2
/**
 * Entity class. This holds only the id and name. The components are located on
 * the {@link com.gemengine.system.ComponentSystem}. The operations performed on
 * this entity are called in the {@link com.gemengine.system.EntitySystem}
 * 
 * @author Dragos
 *
 */
public class Entity {
	/**
	 * Global last id , used to generate an unique id for each object.
	 */
	private static int lastId;
	@Getter
	/**
	 * This is used internally to represent entities.
	 */
	private final int id;
	@Getter
	/**
	 * This is used internally to represent entities. The name has to be unique.
	 */
	private final String name;
	/**
	 * Used to get the components of this entity.
	 */
	private final ComponentSystem componentSystem;
	/**
	 * Used to do operations on entities.
	 */
	private final EntitySystem entitySystem;

	/**
	 * Construct a new entity based on the name.
	 * 
	 * @param name
	 *            The name of the new entity. This has to be unique amongst
	 *            other entities.
	 * @param entitySystem
	 *            Used to do operations on entities
	 * @param componentSystem
	 *            Used to get the components of this entity
	 */
	public Entity(String name, EntitySystem entitySystem, ComponentSystem componentSystem) {
		this.name = name;
		this.id = lastId++;
		this.componentSystem = componentSystem;
		this.entitySystem = entitySystem;
	}

	/**
	 * Add the child entity to this entity children.
	 * 
	 * @param child
	 *            The child to be added
	 */
	public void addChild(Entity child) {
		entitySystem.setParent(this, child);
	}

	/**
	 * Add a parent to this entity.
	 * 
	 * @param parent
	 *            The entity to be parented to
	 */
	public void addParent(Entity parent) {
		entitySystem.setParent(parent, this);
	}

	/**
	 * Create a new component of the given type for this entity. The type must
	 * not represent an interface or an abstract class. If the type already
	 * exists as a component, returns null.
	 * 
	 * @param type
	 *            The class type of the new component.
	 * @return New component or null.
	 */
	public <T extends Component> T createComponent(Class<T> type) {
		return componentSystem.create(this, type);
	}

	/**
	 * Delete this entity
	 */
	public void delete() {
		entitySystem.delete(this);
	}

	/**
	 * Delete a component of the given type from this entity.
	 * 
	 * @param type
	 *            The class type of the component.
	 */
	public void deleteComponent(Class<? extends Component> type) {
		componentSystem.remove(this, type);
	}

	/**
	 * Removes this entity from the logical children entities of the parent
	 * entity.
	 */
	public void unlinkParent() {
		entitySystem.unlinkParent(this);
	}

	/**
	 * Get the children of this entity
	 * 
	 * @return The children entities.
	 */
	public Set<Entity> getChildren() {
		return entitySystem.getChildren(this);
	}

	/**
	 * Get a component of the given type or null.
	 * 
	 * @param type
	 *            The class type of the component
	 * @return A component or null
	 */
	public <T extends Component> T getComponent(Class<T> type) {
		List<T> components = componentSystem.get(this, type);
		if (components == null || components.isEmpty()) {
			return null;
		}
		return components.get(0);
	}

	/**
	 * Get a list of components. This is useful if you need components of a
	 * supertype.
	 * 
	 * @param type
	 *            The type of the components.
	 * @return The components or null.
	 */
	public <T extends Component> List<T> getComponents(Class<T> type) {
		return componentSystem.get(this, type);
	}

	/**
	 * Get the children of this entity and the children of those, recursively,
	 * until there are no more children found.
	 * 
	 * @return The descendants.
	 */
	public Set<Entity> getDescendants() {
		return entitySystem.getDescendants(this);
	}

	/**
	 * Get the parent of this entity or null.
	 * 
	 * @return An entity or null.
	 */
	public Entity getParent() {
		return entitySystem.getParent(this);
	}

	/**
	 * Get the predecessors of this entity. This is a collection of this
	 * entities parent and the parent of that one, recursively until no more
	 * parents are found.
	 * 
	 * @return The predecessors.
	 */
	public Set<Entity> getPredecessors() {
		return entitySystem.getPredecessors(this);
	}

	/**
	 * Whether this entity has a parent or not.
	 * 
	 * @return True if this entity is a child.
	 */
	public boolean isChild() {
		return entitySystem.hasParent(this);
	}

	/**
	 * Whether this entity has children or not.
	 * 
	 * @return True if this entity is a parent.
	 */
	public boolean isParent() {
		return entitySystem.isParent(this);
	}

	/**
	 * Remove all the logical links between this entity and its children.
	 */
	public void unlinkChildren() {
		entitySystem.unlinkChildren(this);
	}
}
