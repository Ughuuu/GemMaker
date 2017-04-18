package com.gem.engine;

import com.gem.component.ComponentBase;
import com.gem.entity.Entity;

public interface TypeObserver {

    public void Added(ComponentBase obj);

    public void ChangedComponent(ComponentBase obj);

    public void Parented(Entity ent, Entity parent);

    public void Removed(ComponentBase obj);

    public void Reorder(Entity entity, Entity entity2);
}
