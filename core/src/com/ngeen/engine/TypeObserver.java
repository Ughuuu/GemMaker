package com.ngeen.engine;

import com.ngeen.component.ComponentBase;
import com.ngeen.entity.Entity;

public interface TypeObserver {
	
	public void Removed(ComponentBase obj);
	
	public void Added(ComponentBase obj);
	
	public void ChangedComponent(ComponentBase obj);
	
	public void Parented(Entity ent, Entity parent);

	public void Reorder(Entity entity, Entity entity2);
}
