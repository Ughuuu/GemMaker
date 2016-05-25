package com.ngeen.systems;

import com.ngeen.component.ComponentBase;
import com.ngeen.component.ComponentFactory;
import com.ngeen.component.ui.ComponentUIStage;
import com.ngeen.component.ui.UISolver;
import com.ngeen.engine.Ngeen;
import com.ngeen.engine.TypeObserver;
import com.ngeen.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class SystemStage extends SystemBase implements TypeObserver {
    private final ComponentFactory _ComponentBuilder;
    private final UISolver _UISolver;
    private List<ComponentBase> _AddedComponent;
    private List<ComponentBase> _ChangedComponent;
    private List<ComponentBase> _RemovedComponent;

    public SystemStage(Ngeen ng, SystemConfiguration conf, ComponentFactory _ComponentBuilder) {
        super(ng, conf);
        _AddedComponent = new ArrayList<ComponentBase>();
        _ChangedComponent = new ArrayList<ComponentBase>();
        _RemovedComponent = new ArrayList<ComponentBase>();
        this._ComponentBuilder = _ComponentBuilder;
        _ComponentBuilder.addObserver(this);
        _Ng.EntityBuilder.addObserver(this);
        _UISolver = new UISolver();
    }

    @Override
    public void Added(ComponentBase obj) {
        _UISolver.Added(obj);
    }

    @Override
    public void ChangedComponent(ComponentBase obj) {
        _UISolver.ChangedComponent(obj);
    }

    @Override
    public void onUpdate(Entity ent) {
        ComponentUIStage stage = ent.getComponent(ComponentUIStage.class).act();
    }

    @Override
    public void Parented(Entity ent, Entity parent) {
        _UISolver.Parented(ent, parent);
    }

    @Override
    public void Removed(ComponentBase obj) {
        _UISolver.Removed(obj);
    }

    @Override
    public void Reorder(Entity entity, Entity entity2) {
        _UISolver.Reorder(entity, entity2);
    }
}