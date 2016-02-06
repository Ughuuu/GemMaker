package com.ngeen.component.ui;

import java.util.List;

import com.ngeen.component.ComponentBase;
import com.ngeen.engine.TypeObserver;
import com.ngeen.entity.Entity;

public class UISolver implements TypeObserver {

	@Override
	public void Added(ComponentBase obj) {
		Removed(obj);
	}

	@Override
	public void ChangedComponent(ComponentBase obj) {
		Removed(obj);
	}

	<T extends ComponentBase> Entity FirstUILayout(Entity ent, Class<T> cls) {
		Entity parent = ent.getParent();
		if (parent != null && !parent.hasComponent(cls)) {
			parent = FirstUILayout(parent, cls);
		}
		return parent;
	}

	<T extends ComponentBase> List<Entity> FirstUINodes(Entity ent, Class<T> cls) {
		List<Entity> children = ent.getChildren();
		for (Entity child : children) {
			if (!child.hasComponent(cls)) {
				children.remove(child);
				List<Entity> subChildren = child.getChildren();
				for (Entity subChild : subChildren) {
					children.addAll(FirstUINodes(subChild, cls));
				}
				children.addAll(subChildren);
			}
		}
		return children;
	}

	private boolean isUIType(Entity ent) {
		return ent.hasComponent(ComponentUILayout.class) || ent.hasComponent(ComponentUILayout.class);
	}

	@Override
	public void Parented(Entity ent, Entity parent) {
		Entity obj = FirstUILayout(ent, ComponentUILayout.class);
		Entity obj2 = FirstUILayout(ent, ComponentUIStage.class);
		if (obj != null) {
			// solveLayout(obj.getComponent(ComponentUILayout.class));
		}
		if (obj2 != null) {
			// solveStage(obj2.getComponent(ComponentUIStage.class));
		}
	}

	@Override
	public void Removed(ComponentBase obj) {
		if (obj.getSubType().equals(ComponentUILayout.class)) {
			// solveLayout((ComponentUILayout) obj);
		} else if (obj.getType().equals(ComponentUIStage.class)) {
			// solveStage((ComponentUIStage) obj);
		} else if (obj.getType().equals(ComponentUIWidget.class)) {
			// solveStage((ComponentUIStage) obj);
		}
	}

	@Override
	public void Reorder(Entity entity, Entity entity2) {
		Entity obj = FirstUILayout(entity, ComponentUILayout.class);
		Entity obj2 = FirstUILayout(entity, ComponentUIStage.class);
		if (obj != null) {
			// solveLayout(obj.getComponent(ComponentUILayout.class));
		}
		if (obj2 != null) {
			// solveStage(obj2.getComponent(ComponentUIStage.class));
		}
	}

	private void solveLayout(ComponentUILayout layout) {
	}

	private void solveStage(ComponentUIStage stage) {
		List<Entity> childrenLayout = FirstUINodes(stage.getOwner(), ComponentUILayout.class);
		List<Entity> childrenWidget = FirstUINodes(stage.getOwner(), ComponentUIWidget.class);
		childrenLayout.forEach(child -> stage._Stage.addActor(child.getComponent(ComponentUILayout.class)._Layout));
		childrenWidget.forEach(child -> stage._Stage.addActor(child.getComponent(ComponentUIWidget.class)._Widget));
	}
}