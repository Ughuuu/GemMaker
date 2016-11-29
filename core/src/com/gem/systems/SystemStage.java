package com.gem.systems;

import java.util.ArrayList;
import java.util.List;

import com.gem.component.ComponentBase;
import com.gem.component.ComponentFactory;
import com.gem.component.ui.ComponentUIStage;
import com.gem.component.ui.UISolver;
import com.gem.engine.Gem;
import com.gem.engine.TypeObserver;
import com.gem.entity.Entity;

public class SystemStage extends SystemBase implements TypeObserver {
	private final ComponentFactory componentBuilder;
	private final UISolver uiSolver;
	private List<ComponentBase> addedComponent;
	private List<ComponentBase> changedComponent;
	private List<ComponentBase> removedComponent;

	public SystemStage(Gem ng, SystemConfiguration conf, ComponentFactory _ComponentBuilder) {
		super(ng, conf);
		addedComponent = new ArrayList<ComponentBase>();
		changedComponent = new ArrayList<ComponentBase>();
		removedComponent = new ArrayList<ComponentBase>();
		this.componentBuilder = _ComponentBuilder;
		_ComponentBuilder.addObserver(this);
		gem.entityBuilder.addObserver(this);
		uiSolver = new UISolver();
	}

	@Override
	public void Added(ComponentBase obj) {
		uiSolver.Added(obj);
	}

	@Override
	public void ChangedComponent(ComponentBase obj) {
		uiSolver.ChangedComponent(obj);
	}

	@Override
	public void onUpdate(Entity ent) {
		ComponentUIStage stage = ent.getComponent(ComponentUIStage.class).act();
	}

	@Override
	public void Parented(Entity ent, Entity parent) {
		uiSolver.Parented(ent, parent);
	}

	@Override
	public void Removed(ComponentBase obj) {
		uiSolver.Removed(obj);
	}

	@Override
	public void Reorder(Entity entity, Entity entity2) {
		uiSolver.Reorder(entity, entity2);
	}
}